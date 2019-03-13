package com.cfh.fatmeasurementsbackend.common;

import com.cfh.fatmeasurementsbackend.constant.ResponseCodeEnum;
import lombok.Data;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
@Data
public class ResponseView<R> {
    private int code = ResponseCodeEnum.OK.getCode();
    private String message;
    private R result;
}
