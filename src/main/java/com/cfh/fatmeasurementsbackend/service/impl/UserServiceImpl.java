package com.cfh.fatmeasurementsbackend.service.impl;

import com.cfh.fatmeasurementsbackend.common.ResponseView;
import com.cfh.fatmeasurementsbackend.constant.ResponseCodeEnum;
import com.cfh.fatmeasurementsbackend.dao.domain.User;
import com.cfh.fatmeasurementsbackend.dao.repository.UserRepository;
import com.cfh.fatmeasurementsbackend.pojo.dto.UserDto;
import com.cfh.fatmeasurementsbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseView loginAuth(String userName, String userPassword) {
        User loginUser = userRepository.findByUserName(userName);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(loginUser, userDto);
        ResponseView<UserDto> responseView = new ResponseView<>();

        if (loginUser == null) {
            responseView.setCode(ResponseCodeEnum.NO_USER.getCode());
            responseView.setMessage(ResponseCodeEnum.NO_USER.getValue());
        }

        if (loginUser.getUserPassword().equals(userPassword)) {
            responseView.setCode(ResponseCodeEnum.OK.getCode());
            responseView.setMessage(ResponseCodeEnum.OK.getValue());
            responseView.setResult(userDto);
        } else {
            responseView.setCode(ResponseCodeEnum.PWD_ERROR.getCode());
            responseView.setMessage(ResponseCodeEnum.PWD_ERROR.getValue());
        }

        return responseView;
    }

    @Override
    public UserDto userRegister(String userName, String userPassword) {
        User user = new User();
        user.setUserName(userName);
        user.setUserPassword(userPassword);

        User result = userRepository.save(user);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(result, userDto);

        return userDto;
    }

    @Override
    public UserDto queryUserByUserName(String userName) {
        UserDto userDto = new UserDto();
        User user = userRepository.findByUserName(userName);

        if (user == null) {
            return userDto;
        }

        BeanUtils.copyProperties(user, userDto);

        return userDto;
    }

    @Override
    public void updateUserHeadPortrait(String url, Long id) {
        User oldUser = userRepository.findById(id).get();
        oldUser.setUserHeadPortrait(url);

        userRepository.save(oldUser);
    }
}