package com.cfh.fatmeasurementsbackend.common;

import lombok.Data;

/**
 * 用户登录后会将用户信息写入这个类中, 目前只存放用户的id信息可以随需求进行扩展
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */
@Data
public class WebUser {
    private Integer userId;

    private static ThreadLocal<WebUser> webUserThreadLocal = new ThreadLocal<WebUser>();

    public static WebUser getWebUser() {
        return webUserThreadLocal.get();
    }

    public static void setWebUser(WebUser webUser) {
        webUserThreadLocal.set(webUser);
    }
}