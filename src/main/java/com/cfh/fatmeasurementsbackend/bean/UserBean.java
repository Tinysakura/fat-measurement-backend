package com.cfh.fatmeasurementsbackend.bean;

import com.cfh.fatmeasurementsbackend.common.ResponseView;
import com.cfh.fatmeasurementsbackend.constant.ResponseCodeEnum;
import com.cfh.fatmeasurementsbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
@RestController
public class UserBean {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/a/login")
    public ResponseView login(@RequestParam(value = "userName") String userName,
                              @RequestParam(value = "userPassword") String userPassword) {
        ResponseView<ResponseView> responseView = new ResponseView<>();

        responseView.setCode(ResponseCodeEnum.OK.getCode());

        ResponseCodeEnum responseCodeEnum = userService.loginAuth(userName, userPassword);
        ResponseView result = new ResponseView();
        result.setCode(responseCodeEnum.getCode());
        result.setMessage(responseCodeEnum.getValue());

        responseView.setResult(result);

        return responseView;
    }
}