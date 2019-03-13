package com.cfh.fatmeasurementsbackend.dao.repository;

import com.cfh.fatmeasurementsbackend.dao.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 根据userName查找用户
     * @param userName
     * @return
     */
    User findByUserName(String userName);
}