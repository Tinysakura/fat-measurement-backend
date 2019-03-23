package com.cfh.fatmeasurementsbackend.bean;

import com.cfh.fatmeasurementsbackend.common.ResponseView;
import com.cfh.fatmeasurementsbackend.common.WebUser;
import com.cfh.fatmeasurementsbackend.constant.ResponseCodeEnum;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataFormDto;
import com.cfh.fatmeasurementsbackend.service.AnimalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */
@RestController
public class AnimalDataBean {

    @Autowired
    private AnimalDataService animalDataService;

    @PostMapping(value = "/a/submit/form/animal/data")
    public ResponseView saveAnimalDataForm(@RequestParam(value = "id", required = false) Long id,
                                           @RequestParam("animalId") String animalId,
                                           @RequestParam("animalWeight")BigDecimal animalWeight,
                                           @RequestParam("animalSex")Integer animalSex,
                                           @RequestParam("animalVariety")Integer animalVariety,
                                           @RequestParam("animalBUltrasound")MultipartFile animalBUltrasound) {
        AnimalDataFormDto animalDataFormDto = new AnimalDataFormDto();

        WebUser webUser = WebUser.getWebUser();
        animalDataFormDto.setUserId(webUser.getUserId());
        animalDataFormDto.setId(id);
        animalDataFormDto.setAnimalId(animalId);
        animalDataFormDto.setAnimalWeight(animalWeight);
        animalDataFormDto.setAnimalSex(animalSex);
        animalDataFormDto.setAnimalVariety(animalVariety);
        animalDataFormDto.setAnimalBUltrasound(animalBUltrasound);

        ResponseView responseView = new ResponseView();

        responseView.setCode(ResponseCodeEnum.OK.getCode());
        responseView.setMessage(ResponseCodeEnum.OK.getValue());
        responseView.setResult(animalDataService.submitAnimalDataForm(animalDataFormDto));

        return responseView;
    }

    @GetMapping(value = "/a/query/animal/data")
    public ResponseView getAnimalDataById(@RequestParam(value = "id") Long id) {
        ResponseView responseView = new ResponseView();

        responseView.setCode(ResponseCodeEnum.OK.getCode());
        responseView.setMessage(ResponseCodeEnum.OK.getValue());

        responseView.setResult(animalDataService.getAnimalDataById(id));

        return responseView;
    }
}