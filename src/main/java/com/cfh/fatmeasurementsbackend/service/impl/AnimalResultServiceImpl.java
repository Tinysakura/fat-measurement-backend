package com.cfh.fatmeasurementsbackend.service.impl;

import com.cfh.fatmeasurementsbackend.dao.domain.AnimalResult;
import com.cfh.fatmeasurementsbackend.dao.repository.AnimalResultRepository;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataDto;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalResultDto;
import com.cfh.fatmeasurementsbackend.pojo.vo.AnimalDetailVo;
import com.cfh.fatmeasurementsbackend.service.AnimalDataService;
import com.cfh.fatmeasurementsbackend.service.AnimalResultService;
import com.sun.javafx.binding.StringFormatter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */
@Service
@Slf4j
public class AnimalResultServiceImpl implements AnimalResultService {
    @Autowired
    private AnimalResultRepository animalResultRepository;

    @Autowired
    private AnimalDataService animalDataService;

    /**
     * 为了耗时方法执行的幂等性使用分布式锁
     */
    @Autowired
    private RedissonClient redissonClient;

    private static String resourcePath = System.getProperty("user.dir");

    @Resource(name = "normalThreadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private String distributedKeyFormat = "animal_data_measure_key_%s";

    @Override
    public AnimalResultDto getAnimalResultByAnimalDataId(Long animalDataId) {
        AnimalResultDto animalResultDto = new AnimalResultDto();

        AnimalResult animalResult = animalResultRepository.getAnimalResultByAnimalDataId(animalDataId);

        BeanUtils.copyProperties(animalResult, animalResultDto);

        return animalResultDto;
    }

    @Override
    public List<AnimalDetailVo> getAnimalResultByUserId(Long userId) {
        /**
         * 先查出用户所有的animal data数据
         */
        List<AnimalDataDto> animalDataDtoList = animalDataService.getAnimalDataByUserId(userId);

        List<Long> animalDataIdList = animalDataDtoList.stream().map(AnimalDataDto::getId).collect(Collectors.toList());

        List<AnimalResult> animalResultList = animalResultRepository.findByAnimalDataIdIn(animalDataIdList);

        List<AnimalDetailVo> animalDetailVoList = new ArrayList<>();

        /**
         * map key:animal_data_id -> value:animalResult
         */
        Map<Long, AnimalResult> animalResultMap = animalResultList.stream().collect(Collectors.toMap(AnimalResult::getAnimalDataId, e -> e));

        for (AnimalDataDto animalDataDto : animalDataDtoList) {
            AnimalDetailVo animalDetailVo = new AnimalDetailVo();

            AnimalResultDto animalResultDto = new AnimalResultDto();
            BeanUtils.copyProperties(animalResultMap.get(animalDataDto.getId()), animalResultDto);

            animalDetailVo.setAnimalDataDto(animalDataDto);
            animalDetailVo.setAnimalResultDto(animalResultDto);
        }

        return animalDetailVoList;
    }

    @Override
    public AnimalResultDto measureAnimalData(Long animalDataId) {
        String lockKey = StringFormatter.format(distributedKeyFormat, animalDataId).getValue();

        String bmp = null;

        /**
         * 从nos将b超文件下载到本地，测量完成之后删除
         */

        AnimalResultDto result = executorWithLock(lockKey, e -> {
            AnimalResultDto animalResultDto = new AnimalResultDto();
            AnimalResult animalResult = new AnimalResult();

            /**
             * 异步同时测量眼肌面积，背膘厚度，脂肪含量
             */
            Future<BigDecimal> musculiOculiFuture = threadPoolTaskExecutor.submit(() -> measureMusculiOculi(bmp));
            Future<BigDecimal> backFatFuture = threadPoolTaskExecutor.submit(() -> measureBackFat(bmp));
            Future<BigDecimal> fatRateFuture = threadPoolTaskExecutor.submit(() -> measureFatRate(bmp));

            BigDecimal musculiOculi = null;
            BigDecimal backFat = null;
            BigDecimal fatRate = null;

            try {
                musculiOculi = musculiOculiFuture.get();
                backFat = backFatFuture.get();
                fatRate = fatRateFuture.get();
            } catch (InterruptedException | ExecutionException e1) {
                e1.printStackTrace();
            }

            animalResult.setAnimalDataId(animalDataId);
            animalResult.setBackFat(measureBackFat(bmp));
            animalResult.setBackFatRevise(backFatRevise(backFat));
            animalResult.setMusculiOculi(musculiOculi);
            animalResult.setMusculiOculiRevise(musculiOculiRevise(musculiOculi));
            animalResult.setFatRate(fatRate);
            animalResult.setLeanRate(caculateLeanRate());
            animalResult.setFatBalanceRank(caculateFatBalanceRank());

            /**
             * 保存计算结果到数据库
             */
            AnimalResult saveResult = animalResultRepository.save(animalResult);
            BeanUtils.copyProperties(saveResult, animalResultDto);

            return animalResultDto;
        });

        return result;
    }

    /**
     * 使用py算法模型测量眼肌面积
     * @return
     */
    private BigDecimal measureMusculiOculi(String bmp) {
        String bmpFilePath = "./bmp/".concat(bmp);
        String[] command = new String[]{"./1015.sh", "bmpFilePath"};

        try {
            return new BigDecimal(invokeExternal(command));
        } catch (IOException e) {
            e.printStackTrace();
            log.info("测量眼肌面积异常");
            return null;
        }
    }

    /**
     * 使用py算法模型测量背膘厚度
     * @return
     */
    private BigDecimal measureBackFat(String bmp) {
        String bmpFilePath = "./bmp/".concat(bmp);
        String[] command = new String[]{"./1016.sh", bmpFilePath};

        try {
            return new BigDecimal(invokeExternal(command));
        } catch (IOException e) {
            e.printStackTrace();
            log.info("测量背膘厚度异常");
            return null;
        }
    }

    /**
     * 使用py算法模型测量脂肪含量
     * @return
     */
    private BigDecimal measureFatRate(String bmp) {
        String bmpFilePath = "./bmp/".concat(bmp);
        String[] command = new String[]{"./1017.sh", bmpFilePath};

        try {
            return new BigDecimal(invokeExternal(command));
        } catch (IOException e) {
            e.printStackTrace();
            log.info("测量脂肪含量");
            return null;
        }
    }

    @Override
    public void testInvokeExternal() {
        log.info("resourcePath:{}", resourcePath);

        try {
            invokeExternal(new String[]{"./test.sh", "陈飞豪"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String invokeExternal(String[] command) throws IOException {
        // 改变当前目录执行shell脚本
        Process process = Runtime.getRuntime().exec(command, null, new File(resourcePath.concat("/src/main/resources/py")));
        InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String invokeResult = bufferedReader.readLine();
        log.info("command:{} invoke, result:{}", command, invokeResult);

        return invokeResult;
    }

    private BigDecimal musculiOculiRevise(BigDecimal original) {
        // TODO 计算眼肌面积校正值
        return null;
    }

    private BigDecimal backFatRevise(BigDecimal original) {
        // TODO 计算背膘厚度校正值
        return null;
    }

    private BigDecimal caculateLeanRate() {
        // TODO 计算瘦肉率
        return null;
    }

    private Integer caculateFatBalanceRank() {
        // TODO 计算脂肪均匀等级
        return null;
    }

    private  <R> R executorWithLock(String lockKey, Function<Object, R> function) {
        RLock rLock = redissonClient.getLock(lockKey);
        try {
            boolean lock = rLock.tryLock(1000, TimeUnit.MILLISECONDS);
            if (lock) {
                log.info("获取到分布式锁:{}", lockKey);
                return function.apply(lockKey);
            }

        } catch (InterruptedException e) {
            log.info("发生异常，释放分布式锁:{}", lockKey);
            rLock.unlock();
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }

        return null;
    }
}