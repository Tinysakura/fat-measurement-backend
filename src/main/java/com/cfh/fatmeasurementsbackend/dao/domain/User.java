package com.cfh.fatmeasurementsbackend.dao.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
@Entity
@Table(name = "user")
@Data
@EntityListeners(AuditingEntityListener.class)
public class User {
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

    @Column(name = "user_name", columnDefinition = "VARCHAR")
    private String userName;

    @Column(name = "user_password", columnDefinition = "VARCHAR")
    private String userPassword;

    @Column(name = "user_head_portrait", columnDefinition = "VARCHAR")
    private String userHeadPortrait;

}