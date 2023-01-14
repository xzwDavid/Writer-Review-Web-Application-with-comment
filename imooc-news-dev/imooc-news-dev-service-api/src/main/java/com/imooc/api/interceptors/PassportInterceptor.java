package com.imooc.api.interceptors;

//import com.imooc.exception.GraceException;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.IPUtil;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    public RedisOperator redis;
    public static final String MOBILE_SMSCODE = "mobile:smscode";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        redis = new RedisOperator();
        String userIp = IPUtil.getRequestIp(request);
       boolean keyIsExist = redis.keyIsExist(MOBILE_SMSCODE + ":" + userIp);
       if(keyIsExist){
           GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
           //System.out.println("短信发送频率过大！");
           return false;

       }
    //System.out.println("\n\n\n\ndone2\n\n\n\n");
       return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
