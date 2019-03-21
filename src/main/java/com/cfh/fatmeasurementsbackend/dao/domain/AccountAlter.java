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
@Table(name = "account_alter")
@EntityListeners(AuditingEntityListener.class)
@Data
public class AccountAlter {

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

    @Column(name = "account_alter_type")
    private Integer accountAlterType;

    @Column(name = "account_alter_amount")
    private BigDecimal accountAlterAmount;

    @Column(name = "account_alter_comment", columnDefinition = "VARCHAR")
    private String accountAlterComment;
}