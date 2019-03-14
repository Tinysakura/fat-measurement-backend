package com.cfh.fatmeasurementsbackend.bean;

import com.cfh.fatmeasurementsbackend.common.ResponseView;
import com.cfh.fatmeasurementsbackend.common.WebUser;
import com.cfh.fatmeasurementsbackend.constant.ResponseCodeEnum;
import com.cfh.fatmeasurementsbackend.service.AnimalResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */
@RestController
public class AnimalResultBean {

    @Autowired
    private AnimalResultService animalResultService;

    @GetMapping("/a/query/fat/analyze/result")
    public ResponseView queryFatAnalyzeResult(WebUser webUser) {
        ResponseView responseView = new ResponseView();

        responseView.setCode(ResponseCodeEnum.OK.getCode());
        responseView.setMessage(ResponseCodeEnum.OK.getValue());
        responseView.setResult(animalResultService.getAnimalResultByUserId(webUser.getUserId()));

        return responseView;
    }
}