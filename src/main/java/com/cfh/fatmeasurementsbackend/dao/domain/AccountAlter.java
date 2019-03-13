package com.cfh.fatmeasurementsbackend.dao.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
@Entity
@Table(name = "account_alter")
@Data
public class AccountAlter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "db_create_time")
    private Long dbCreateTime;

    @Column(name = "db_update_time")
    private Long dbUpdateTime;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "account_alter_type")
    private Integer accountAlterType;

    @Column(name = "account_alter_amount")
    private BigDecimal accountAlterAmount;

    @Column(name = "account_alter_comment")
    private String accountAlterComment;
}