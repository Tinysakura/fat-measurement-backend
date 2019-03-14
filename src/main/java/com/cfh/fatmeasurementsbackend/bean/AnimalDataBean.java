package com.cfh.fatmeasurementsbackend.bean;

import com.cfh.fatmeasurementsbackend.common.ResponseView;
import com.cfh.fatmeasurementsbackend.common.WebUser;
import com.cfh.fatmeasurementsbackend.constant.ResponseCodeEnum;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataFormDto;
import com.cfh.fatmeasurementsbackend.service.AnimalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */
@RestController
public class AnimalDataBean {

    @Autowired
    private AnimalDataService animalDataService;

    @PostMapping(value = "/a/submit/form/animal/data")
    public ResponseView saveAnimalDataForm(@RequestBody AnimalDataFormDto animalDataFormDto,
                                           WebUser webUser) {

        animalDataFormDto.setUserId(webUser.getUserId());

        ResponseView responseView = new ResponseView();

        responseView.setCode(ResponseCodeEnum.OK.getCode());
        responseView.setMessage(ResponseCodeEnum.OK.getValue());
        responseView.setResult(animalDataService.submitAnimalDataForm(animalDataFormDto));

        return responseView;
    }
}