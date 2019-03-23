package com.cfh.fatmeasurementsbackend.dao.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
@Entity
@Table(name = "animal_data")
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "data_animal_nos_key", columnDefinition = "VARCHAR")
    private String nosKey;

    /**
     * 测量动物编号
     */
    @Column(name = "data_animal_id", columnDefinition = "VARCHAR")
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

    /**
     * 该条数据是否已结果测量(测量完成的数据不能被修改)
     */
    @Column(name = "data_animal_draft")
    private Integer animalDraft;

}