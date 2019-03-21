package com.cfh.fatmeasurementsbackend.bean;

import com.cfh.fatmeasurementsbackend.common.ResponseView;
import com.cfh.fatmeasurementsbackend.common.WebUser;
import com.cfh.fatmeasurementsbackend.constant.ResponseCodeEnum;
import com.cfh.fatmeasurementsbackend.pojo.dto.UserDto;
import com.cfh.fatmeasurementsbackend.service.NosService;
import com.cfh.fatmeasurementsbackend.service.OssService;
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

//    @Autowired
//    private NosService nosService;

    @Autowired
    private OssService ossService;

    @GetMapping(value = "/a/login")
    public ResponseView login(@RequestParam(value = "userName") String userName,
                              @RequestParam(value = "userPassword") String userPassword) {
        ResponseView<ResponseView> responseView = new ResponseView<>();

        responseView.setCode(ResponseCodeEnum.OK.getCode());
        responseView.setResult(userService.loginAuth(userName, userPassword));

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
            String url = ossService.uploadHeadportrait2Oss(headportrait.getInputStream());
            responseView.setCode(ResponseCodeEnum.OK.getCode());
            responseView.setMessage("上传成功");
            responseView.setResult(url);

            WebUser webUser = WebUser.getWebUser();
            userService.updateUserHeadPortrait(url, webUser.getUserId());
        } catch (IOException e) {
            e.printStackTrace();
            responseView.setCode(ResponseCodeEnum.ERROR.getCode());
            responseView.setMessage("上传失败请重试");
        }

        return responseView;
    }

    @GetMapping(value = "/a/get/headportrait")
    public ResponseView getUserHeadPortrait(@RequestParam(value = "userName")String userName) {
        ResponseView responseView = new ResponseView();

        responseView.setCode(ResponseCodeEnum.OK.getCode());
        responseView.setMessage(ResponseCodeEnum.OK.getValue());
        responseView.setResult(userService.queryUserByUserName(userName).getUserHeadPortrait());

        return responseView;
    }

    @PostMapping(value = "/a/update/user/info")
    public ResponseView updateUserInfo(@RequestBody UserDto userDto) {
        ResponseView responseView = new ResponseView();

        responseView.setCode(ResponseCodeEnum.OK.getCode());
        responseView.setMessage(ResponseCodeEnum.OK.getValue());

        userService.updateUserInfo(userDto);

        return responseView;
    }
}