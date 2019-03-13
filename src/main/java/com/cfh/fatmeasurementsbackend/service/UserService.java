package com.cfh.fatmeasurementsbackend.service;

import com.cfh.fatmeasurementsbackend.constant.ResponseCodeEnum;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
public interface UserService {
    ResponseCodeEnum loginAuth(String userName, String userPassword);
}