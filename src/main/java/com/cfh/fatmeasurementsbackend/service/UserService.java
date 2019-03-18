package com.cfh.fatmeasurementsbackend.service;

import com.cfh.fatmeasurementsbackend.common.ResponseView;
import com.cfh.fatmeasurementsbackend.dao.domain.User;
import com.cfh.fatmeasurementsbackend.pojo.dto.UserDto;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
public interface UserService {
    ResponseView loginAuth(String userName, String userPassword);

    UserDto userRegister(String userName, String userPassword);

    UserDto queryUserByUserName(String userName);
}