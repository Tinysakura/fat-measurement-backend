package com.cfh.fatmeasurementsbackend.service.impl;

import com.cfh.fatmeasurementsbackend.constant.AnimalConstant;
import com.cfh.fatmeasurementsbackend.dao.domain.AnimalData;
import com.cfh.fatmeasurementsbackend.dao.domain.AnimalResult;
import com.cfh.fatmeasurementsbackend.dao.repository.AnimalDataRepository;
import com.cfh.fatmeasurementsbackend.dao.repository.AnimalResultRepository;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataDto;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalResultDto;
import com.cfh.fatmeasurementsbackend.pojo.vo.AnimalDetailVo;
import com.cfh.fatmeasurementsbackend.service.AnimalDataService;
import com.cfh.fatmeasurementsbackend.service.AnimalResultService;
import com.cfh.fatmeasurementsbackend.service.OssService;
import com.cfh.fatmeasurementsbackend.util.RedissionUtil;
import com.sun.javafx.binding.StringFormatter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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
    private AnimalDataRepository animalDataRepository;

    @Autowired
    private AnimalDataService animalDataService;

    /**
     * 为了耗时方法执行的幂等性使用分布式锁
     */
    @Autowired
    private RedissonClient redissonClient;

//    @Autowired
//    private NosService nosService;

    @Autowired
    private OssService ossService;

    @Resource(name = "normalThreadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private String distributedKeyFormat = "animal_data_measure_key_%s";

    @Override
    public AnimalResultDto getAnimalResultByAnimalDataId(Long animalDataId) {
        AnimalResultDto animalResultDto = new AnimalResultDto();

        AnimalResult animalResult = animalResultRepository.getAnimalResultByAnimalDataId(animalDataId);

        if (animalResult != null) {
            BeanUtils.copyProperties(animalResult, animalResultDto);
        }

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

        /**
         * 从nos将b超文件下载到本地，测量完成之后删除
         * b超文件名为对应的nos-key加上uuid, 后缀为BMP, 统一保存在py文件所在路径的下级bmp路径下
         */
        AnimalDataDto animalData = animalDataService.getAnimalDataById(animalDataId);
        String bmp = animalData.getNosKey();
        File sourceFile = null;
        try {
            log.info("将{}对应的B超文件下载到临时目录", animalDataId, "classpath:".concat(bmp));
            org.springframework.core.io.Resource resource = new ClassPathResource("py/bmp/".concat(bmp));
            sourceFile =  resource.getFile();
            ossService.downloadBUltrasonicFromOss(sourceFile, animalData.getNosKey());
        } catch (IOException e) {
            log.info("{}对应的B超文件下载失败", animalDataId);
            e.printStackTrace();
        }

        AnimalResultDto result = RedissionUtil.executorWithLock(redissonClient, lockKey, e -> {
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

//            musculiOculi = measureMusculiOculi(bmp);
//            backFat = measureBackFat(bmp);
//            fatRate = measureFatRate(bmp);

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
             * 更新animal_data状态:draft -> measured
             */
            AnimalResult saveResult = animalResultRepository.save(animalResult);
            AnimalData saveAnimalData = new AnimalData();
            BeanUtils.copyProperties(animalData, saveAnimalData);
            saveAnimalData.setAnimalDraft(AnimalConstant.AnimalDraftEnum.NOT_DRAFT.getCode());
            animalDataRepository.save(saveAnimalData);

            BeanUtils.copyProperties(saveResult, animalResultDto);

            return animalResultDto;
        });

        /**
         * 删除临时目录下下载的B超文件
         */
        try {
            log.info("删除临时目录下的B超文件{}", "classpath:".concat(bmp));
            try {
                sourceFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 使用py算法模型测量眼肌面积
     * @return
     */
    private BigDecimal measureMusculiOculi(String bmp) {
        String bmpFilePath = "./bmp/".concat(bmp);
        String[] command = new String[]{"./1015.sh", bmpFilePath};

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
        org.springframework.core.io.Resource resource = new ClassPathResource("py");
        File sourceFile =  resource.getFile();
        Process process = Runtime.getRuntime().exec(command, null, sourceFile);
        InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String invokeResult = null;
        while (bufferedReader.readLine() != null) {
            invokeResult = bufferedReader.readLine();
        }
        log.info("command:{} invoke, result:{}", command, invokeResult);

        return invokeResult;
    }

    private BigDecimal musculiOculiRevise(BigDecimal original) {
        // TODO 计算眼肌面积校正值
        return original;
    }

    private BigDecimal backFatRevise(BigDecimal original) {
        // TODO 计算背膘厚度校正值
        return original;
    }

    private BigDecimal caculateLeanRate() {
        // TODO 计算瘦肉率
        return new BigDecimal(0.5);
    }

    private Integer caculateFatBalanceRank() {
        // TODO 计算脂肪均匀等级
        int randomRank = (int)(1+Math.random()*(3));
        return randomRank;
    }
}