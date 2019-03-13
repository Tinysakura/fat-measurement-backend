package com.cfh.fatmeasurementsbackend.dao.domain;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
@Entity
@Table(name = "user")
@Data
public class AnimalResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "db_create_time")
    private Long dbCreateTime;

    @Column(name = "db_update_time")
    private Long dbUpdateTime;

    /**
     * 对应动物数据id
     */
    @Column(name = "animal_data_id")
    private Long animalDataId;

    /**
     * 背膘厚度
     */
    @Column(name = "result_back_fat")
    private BigDecimal backFat;

    /**
     * 背膘厚度校正值
     */
    @Column(name = "result_back_fat_revise")
    private BigDecimal backFatRevise;

    /**
     * 眼肌面积
     */
    @Column(name = "result_musculi_oculi")
    private BigDecimal musculiOculi;

    /**
     * 眼肌面积校正值
     */
    @Column(name = "result_musculi_oculi_revise")
    private BigDecimal musculiOculiRevise;

    /**
     * 瘦肉率
     */
    @Column(name = "result_lean_rate")
    private BigDecimal leanRate;

    /**
     * 肌间脂肪比
     */
    @Column(name = "result_fat_rate")
    private BigDecimal fatRate;

    /**
     * 脂肪均匀等级
     */
    @Column(name = "result_fat_balance_rank")
    private Integer fatBalanceRank;
}