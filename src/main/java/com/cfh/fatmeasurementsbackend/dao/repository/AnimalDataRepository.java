package com.cfh.fatmeasurementsbackend.dao.repository;

import com.cfh.fatmeasurementsbackend.dao.domain.AnimalData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */

public interface AnimalDataRepository extends JpaRepository<AnimalData, Long> {
    List<AnimalData> getByUserId(Long userId);

    AnimalData getById(Long id);
}