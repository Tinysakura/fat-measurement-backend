package com.cfh.fatmeasurementsbackend.pojo.vo;

import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalDataDto;
import com.cfh.fatmeasurementsbackend.pojo.dto.AnimalResultDto;
import lombok.Data;

/**
 * 包含了动物测量数据与对应的测量结果的vo
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */
@Data
public class AnimalDetailVo {
    private AnimalDataDto animalDataDto;

    private AnimalResultDto animalResultDto;
}