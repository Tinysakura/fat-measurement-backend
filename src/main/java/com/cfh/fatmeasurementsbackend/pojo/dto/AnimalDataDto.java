package com.cfh.fatmeasurementsbackend.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */
@Data
public class AnimalDataDto {
    private Long id;

    private Long dbCreateTime;

    private Long dbUpdateTime;

    private Long userId;

    private String animalBUltrasound;

    private String animalId;

    private BigDecimal animalWeight;

    private Integer animalSex;

    private Integer animalVariety;

    private Integer animalDraft;
}