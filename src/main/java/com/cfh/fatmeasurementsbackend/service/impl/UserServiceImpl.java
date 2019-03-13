package com.cfh.fatmeasurementsbackend.service.impl;

import com.cfh.fatmeasurementsbackend.constant.ResponseCodeEnum;
import com.cfh.fatmeasurementsbackend.dao.domain.User;
import com.cfh.fatmeasurementsbackend.dao.repository.UserRepository;
import com.cfh.fatmeasurementsbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseCodeEnum loginAuth(String userName, String userPassword) {
        User loginUser = userRepository.findByUserName(userName);

        if (loginUser == null) {
            return ResponseCodeEnum.NO_USER;
        }

        if (loginUser.getUserPassword().equals(userPassword)) {
            return ResponseCodeEnum.OK;
        } else {
            return ResponseCodeEnum.PWD_ERROR;
        }
    }
}