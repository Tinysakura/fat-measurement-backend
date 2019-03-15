package com.cfh.fatmeasurementsbackend.service;

import com.cfh.fatmeasurementsbackend.FatMeasurementsBackendApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/15
 */

public class TestAnimalResultService extends FatMeasurementsBackendApplicationTests {

    @Autowired
    private AnimalResultService animalResultService;

    @Test
    public void testInvokeExternal() {
        animalResultService.testInvokeExternal();
    }
}