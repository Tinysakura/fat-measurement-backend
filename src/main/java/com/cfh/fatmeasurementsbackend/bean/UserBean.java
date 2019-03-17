package com.cfh.fatmeasurementsbackend.bean;

import com.cfh.fatmeasurementsbackend.common.ResponseView;
import com.cfh.fatmeasurementsbackend.constant.ResponseCodeEnum;
import com.cfh.fatmeasurementsbackend.pojo.dto.UserDto;
import com.cfh.fatmeasurementsbackend.service.NosService;
import com.cfh.fatmeasurementsbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
@RestController
public class UserBean {

    @Autowired
    private UserService userService;

    @Autowired
    private NosService nosService;

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

    @PostMapping(value = "/a/register")
    public ResponseView register(@RequestBody UserDto userDto) {
        ResponseView responseView = new ResponseView();
        responseView.setCode(ResponseCodeEnum.OK.getCode());
        responseView.setResult(userService.userRegister(userDto.getUserName(), userDto.getUserPassword()));

        return responseView;
    }

    @PostMapping(value = "/a/set/headportrait")
    public ResponseView uploadHeadportrait(@RequestParam(value = "headportrait")MultipartFile headportrait) {
        ResponseView responseView = new ResponseView();

        try {
            String url = nosService.uploadHeadportrait2Nos(headportrait.getInputStream());
            responseView.setCode(ResponseCodeEnum.OK.getCode());
            responseView.setMessage("上传成功");
            responseView.setResult(url);
        } catch (IOException e) {
            e.printStackTrace();
            responseView.setCode(ResponseCodeEnum.ERROR.getCode());
            responseView.setMessage("上传失败请重试");
        }

        return responseView;
    }
}