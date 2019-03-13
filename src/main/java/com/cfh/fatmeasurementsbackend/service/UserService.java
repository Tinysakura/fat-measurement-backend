package com.cfh.fatmeasurementsbackend.service;

import com.cfh.fatmeasurementsbackend.constant.ResponseCodeEnum;
import com.cfh.fatmeasurementsbackend.dao.domain.User;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
public interface UserService {
    ResponseCodeEnum loginAuth(String userName, String userPassword);

    User userRegister(String userName, String userPassword);
}