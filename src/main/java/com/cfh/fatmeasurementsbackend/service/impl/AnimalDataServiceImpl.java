package com.cfh.fatmeasurementsbackend.service.impl;

import com.cfh.fatmeasurementsbackend.dao.domain.AnimalData;
import com.cfh.fatmeasurementsbackend.dao.repository.AnimalDataRepository;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataDto;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataFormDto;
import com.cfh.fatmeasurementsbackend.service.AnimalDataService;
import com.cfh.fatmeasurementsbackend.service.NosService;
import com.cfh.fatmeasurementsbackend.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    public AnimalDataDto submitAnimalDataForm(AnimalDataFormDto animalDataFormDto) {
        AnimalData animalData = new AnimalData();

        BeanUtils.copyProperties(animalDataFormDto, animalData);

        /**
         * 将表单中携带的B超数据上传到nos桶中
         */
        try {
            Map<String, String> resultMap = ossService.uploadBUltrasonic2Oss(animalDataFormDto.getAnimalBUltrasound().getInputStream());
            // String url = resultMap.get("url");
            String ossKey = resultMap.get("oss_key");

            log.info("B超文件上传成功:{}", ossKey);
            animalData.setNosKey(ossKey);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("B超文件上传失败");
        }

        AnimalData animalDataResult = animalDataRepository.save(animalData);
        AnimalDataDto animalDataDto = new AnimalDataDto();

        BeanUtils.copyProperties(animalDataResult, animalDataDto);

        return animalDataDto;
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