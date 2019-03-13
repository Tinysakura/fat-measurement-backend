package com.cfh.fatmeasurementsbackend.filter;

import com.alibaba.fastjson.JSON;
import com.cfh.fatmeasurementsbackend.common.ResponseView;
import com.cfh.fatmeasurementsbackend.common.WebUser;
import com.cfh.fatmeasurementsbackend.constant.CommonCookieKeyEnum;
import com.cfh.fatmeasurementsbackend.constant.ResponseCodeEnum;
import com.cfh.fatmeasurementsbackend.util.CookieUtil;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/13
 */

public class AuthFilter implements Filter {

    private static String[] noNeedLoginURL = {"/a/login"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /**
         * 如果为不需要登录即可使用的端点则直接放行
         */
        String URL = ((HttpServletRequest) servletRequest).getRequestURI();
        if (Arrays.asList(noNeedLoginURL).contains(URL)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        WebUser webUser = getWebUserFromCookie((HttpServletRequest) servletRequest);

        if (webUser != null) {
            WebUser.setWebUser(webUser);
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            noLoginResponse((HttpServletResponse) servletResponse);
        }
    }

    private WebUser getWebUserFromCookie(HttpServletRequest servletRequest) {
        WebUser webUser = new WebUser();
        Map<String, Cookie> cookieMap = CookieUtil.readCookieToMap(servletRequest);

        if (cookieMap.get(CommonCookieKeyEnum.LOGIN_ID.getValue()) != null) {
            webUser.setUserId(Long.valueOf(cookieMap.get(CommonCookieKeyEnum.LOGIN_ID.getValue()).getValue()));
            return webUser;
        }

        /**
         * 如果cookie中没有则从header中获取
         */
        webUser.setUserId(Long.valueOf(servletRequest.getHeader(CommonCookieKeyEnum.LOGIN_ID.getValue())));
        return webUser;
    }

    private void noLoginResponse(HttpServletResponse servletResponse) {
        ResponseView responseView = new ResponseView();
        responseView.setCode(ResponseCodeEnum.NO_LOGIN.getCode());
        responseView.setMessage(ResponseCodeEnum.NO_LOGIN.getValue());

        String jsonStr = JSON.toJSONString(responseView);

        try {
            servletResponse.getOutputStream().write(jsonStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}