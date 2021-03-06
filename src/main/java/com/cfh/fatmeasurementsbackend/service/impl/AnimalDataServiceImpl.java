package com.cfh.fatmeasurementsbackend.service.impl;

import com.cfh.fatmeasurementsbackend.common.WebUser;
import com.cfh.fatmeasurementsbackend.constant.AnimalConstant;
import com.cfh.fatmeasurementsbackend.dao.domain.AnimalData;
import com.cfh.fatmeasurementsbackend.dao.repository.AnimalDataRepository;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataDto;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataFormDto;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalResultDto;
import com.cfh.fatmeasurementsbackend.service.AnimalDataService;
import com.cfh.fatmeasurementsbackend.service.NosService;
import com.cfh.fatmeasurementsbackend.service.OssService;
import com.cfh.fatmeasurementsbackend.util.RedissionUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */
@Service
@Slf4j
public class AnimalDataServiceImpl implements AnimalDataService {
    @Autowired
    private AnimalDataRepository animalDataRepository;

//    @Autowired
//    private NosService nosService;

    @Autowired
    private OssService ossService;

    /**
     * 为了耗时方法执行的幂等性使用分布式锁
     */
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public AnimalDataDto submitAnimalDataForm(AnimalDataFormDto animalDataFormDto) {
        /**
         * 使用用户id作为锁，用户只能同时上传一个数据表单
         */
        AnimalDataDto result = RedissionUtil.executorWithLock(redissonClient, String.valueOf(WebUser.getWebUser().getUserId()), e -> {
            AnimalData animalData = new AnimalData();
            AnimalData originalData = null;

            BeanUtils.copyProperties(animalDataFormDto, animalData);

            Long id = animalDataFormDto.getId();
            if (id != null) {
                originalData = animalDataRepository.getById(animalDataFormDto.getId());
            }

            /**
             * 将表单中携带的B超数据上传到nos桶中
             */
            if (animalDataFormDto.getAnimalBUltrasound() != null) {
                try {
                    Map<String, String> resultMap = ossService.uploadBUltrasonic2Oss(animalDataFormDto.getAnimalBUltrasound().getInputStream());
                    // String url = resultMap.get("url");
                    String ossKey = resultMap.get("oss_key");

                    log.info("B超文件上传成功:{}", ossKey);
                    animalData.setNosKey(ossKey);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    log.info("B超文件上传失败");
                }
            } else {
                animalData.setNosKey(originalData.getNosKey());
            }

            animalData.setAnimalDraft(AnimalConstant.AnimalDraftEnum.DRAFT.getCode());
            AnimalData animalDataResult = animalDataRepository.save(animalData);
            AnimalDataDto animalDataDto = new AnimalDataDto();

            BeanUtils.copyProperties(animalDataResult, animalDataDto);

            return animalDataDto;
        });

        return result;
    }

    @Override
    public List<AnimalDataDto> getAnimalDataByUserId(Long userId) {
        List<AnimalData> animalDataList = animalDataRepository.getByUserId(userId);

        List<AnimalDataDto> animalDataDtoList = new ArrayList<>(16);

        for (AnimalData animalData : animalDataList) {
            AnimalDataDto animalDataDto = new AnimalDataDto();
            BeanUtils.copyProperties(animalData, animalDataDto);

            animalDataDtoList.add(animalDataDto);
        }

        return animalDataDtoList;
    }

    @Override
    public AnimalDataDto getAnimalDataById(Long id) {
        AnimalData animalData = animalDataRepository.getById(id);

        AnimalDataDto animalDataDto = new AnimalDataDto();
        BeanUtils.copyProperties(animalData, animalDataDto);

        return animalDataDto;
    }
}