package com.cfh.fatmeasurementsbackend.constant;

/**
 * 通用的cookie键枚举
 *
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
public enum CommonCookieKeyEnum {
    /**
     * 用户登录id
     */
    LOGIN_ID("login-id"),;

    private String value;

    CommonCookieKeyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
