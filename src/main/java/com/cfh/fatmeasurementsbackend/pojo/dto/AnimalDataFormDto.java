package com.cfh.fatmeasurementsbackend.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */
@Data
public class AnimalDataFormDto {
    private Long id;

    private Long userId;

    private MultipartFile animalBUltrasound;

    private String animalId;

    private BigDecimal S;

    private Integer animalSex;

    private Integer animalVariety;
}