package com.cfh.fatmeasurementsbackend.service.impl;

import com.cfh.fatmeasurementsbackend.dao.domain.AnimalData;
import com.cfh.fatmeasurementsbackend.dao.repository.AnimalDataRepository;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataDto;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataFormDto;
import com.cfh.fatmeasurementsbackend.service.AnimalDataService;
import com.cfh.fatmeasurementsbackend.service.NosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */
@Service
@Slf4j
public class AnimalDataServiceImpl implements AnimalDataService {
    @Autowired
    private AnimalDataRepository animalDataRepository;

    @Autowired
    private NosService nosService;

    @Override
    public AnimalDataDto submitAnimalDataForm(AnimalDataFormDto animalDataFormDto) {
        AnimalData animalData = new AnimalData();

        BeanUtils.copyProperties(animalDataFormDto, animalData);

        /**
         * 将表单中携带的B超数据上传到nos桶中
         */
        try {
            String url = nosService.uploadBUltrasonic2Nos(animalDataFormDto.getAnimalBUltrasound().getInputStream());
            log.info("B超文件上传成功:{}", url);
            animalData.setAnimalBUltrasound(url);
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