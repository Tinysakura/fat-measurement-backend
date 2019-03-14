package com.cfh.fatmeasurementsbackend.service;

import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalResultDto;
import com.cfh.fatmeasurementsbackend.pojo.vo.AnimalDetailVo;

import java.util.List;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */

public interface AnimalResultService {
    AnimalResultDto getAnimalResultByAnimalDataId(Long animalDataId);

    List<AnimalDetailVo> getAnimalResultByUserId(Long userId);
}