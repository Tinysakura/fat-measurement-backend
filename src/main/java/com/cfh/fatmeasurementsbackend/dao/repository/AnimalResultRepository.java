package com.cfh.fatmeasurementsbackend.dao.repository;

import com.cfh.fatmeasurementsbackend.dao.domain.AnimalResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */

public interface AnimalResultRepository extends JpaRepository<AnimalResult, Long> {
    AnimalResult getAnimalResultById(Long id);

    AnimalResult getAnimalResultByAnimalDataId(Long animalDataId);

    List<AnimalResult> findByIdIn(List<Long> id);

    List<AnimalResult> findByAnimalDataIdIn(List<Long> animalDataId);
}