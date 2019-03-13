package com.cfh.fatmeasurementsbackend.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * cookie操作工具类
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */

public class CookieUtil {
    public static Map<String, Cookie> readCookieToMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>(16);
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}