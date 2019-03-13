package com.cfh.fatmeasurementsbackend.dao.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
@Entity
@Table(name = "AnimalData")
@Data
public class AnimalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreatedDate
    @Column(name = "db_create_time")
    private Long dbCreateTime;

    @LastModifiedDate
    @Column(name = "db_update_time")
    private Long dbUpdateTime;

    @Column(name = "user_id")
    private Long userId;

    /**
     * B超图片地址
     */
    @Column(name = "data_animal_b_ultrasound")
    private String animalBUltrasound;

    /**
     * 测量动物编号
     */
    @Column(name = "data_animal_id")
    private String animalId;

    /**
     * 测量动物体重
     */
    @Column(name = "data_animal_weight")
    private BigDecimal animalWeight;

    /**
     * 测量动物性别
     */
    @Column(name = "data_animal_sex")
    private Integer animalSex;

    /**
     * 测量动物品种
     */
    @Column(name = "data_animal_variety")
    private Integer animalVariety;

}