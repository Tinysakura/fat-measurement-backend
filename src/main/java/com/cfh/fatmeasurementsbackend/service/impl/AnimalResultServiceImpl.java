package com.cfh.fatmeasurementsbackend.service.impl;

import com.cfh.fatmeasurementsbackend.dao.domain.AnimalResult;
import com.cfh.fatmeasurementsbackend.dao.repository.AnimalResultRepository;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataDto;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalResultDto;
import com.cfh.fatmeasurementsbackend.pojo.vo.AnimalDetailVo;
import com.cfh.fatmeasurementsbackend.service.AnimalDataService;
import com.cfh.fatmeasurementsbackend.service.AnimalResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        // TODO
        return null;
    }
}