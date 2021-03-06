package com.cfh.fatmeasurementsbackend.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */
@Data
public class AnimalResultDto {
    private Long id;

    private Long dbCreateTime;

    private Long dbUpdateTime;

    /**
     * 对应动物数据id
     */
    private Long animalDataId;

    /**
     * 背膘厚度
     */
    private BigDecimal backFat;

    /**
     * 背膘厚度校正值
     */
    private BigDecimal backFatRevise;

    /**
     * 眼肌面积
     */
    private BigDecimal musculiOculi;

    /**
     * 眼肌面积校正值
     */
    private BigDecimal musculiOculiRevise;

    /**
     * 瘦肉率
     */
    private BigDecimal leanRate;

    /**
     * 肌间脂肪比
     */
    private BigDecimal fatRate;

    /**
     * 脂肪均匀等级
     */
    private Integer fatBalanceRank;
}