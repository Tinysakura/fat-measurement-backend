package com.cfh.fatmeasurementsbackend.service;

import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataDto;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataFormDto;

import java.util.List;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */

public interface AnimalDataService {
    AnimalDataDto submitAnimalDataForm(AnimalDataFormDto animalDataFormDto);

    List<AnimalDataDto> getAnimalDataByUserId(Long userId);

    AnimalDataDto getAnimalDataById(Long id);
}