package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.PassportControllerApi;
import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.RegistLoginBO;
import com.imooc.user.service.UserService;
import com.imooc.utils.IPUtil;
import com.imooc.utils.SMSUtils;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("passport")
public class PassportController extends BaseController implements PassportControllerApi {
    final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private UserService userservice;


    @Override
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) {

        String userIp = IPUtil.getRequestIp(request);

        redis.setnx60s(MOBILE_SMSCODE +":"+ userIp,userIp);

        String random = "123789";
        random = (int)((Math.random()*9+1)*100000)+"";
        smsUtils.sendSMS("18699964053",random);
        redis.set(MOBILE_SMSCODE+":"+mobile,random);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult logout(String userId, HttpServletRequest request, HttpServletResponse response) {
        redis.del(REDIS_USER_TOKEN+":"+userId);

        setCookie(request,response,"utoken","",COOKIE_DELETE);
        setCookie(request,response,"uid","",COOKIE_DELETE);
        return null;
    }

    @Override
    public GraceJSONResult doLogin(@Valid RegistLoginBO registLoginBO,
                                   BindingResult result,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        //0. judging whether the bindingresult contains the error
    if (result.hasErrors()){
        Map<String,String> map = getErrors(result);
        return GraceJSONResult.errorMap(map);
    }
    //1. vaild if the msg match
        String mobile = registLoginBO.getMobile();
        String smsCode = registLoginBO.getSmsCode();

        String redisSMSCode = redis.get(MOBILE_SMSCODE+":"+mobile);
        if(StringUtils.isBlank(redisSMSCode)||!redisSMSCode.equalsIgnoreCase(redisSMSCode)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        //查询该用户是否注册
        AppUser user =  userservice.queryMobileIsExist(mobile);
        if(user !=null && user.getActiveStatus() == UserStatus.FROZEN.type){
            //if the user is not empty while the status is freezing forbiding login
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_FROZEN);

        }else if(user == null){// if the user hasn't login
            user = userservice.createUser(mobile);
        }
        // for the distributed session
        int userActiveStatus = user.getActiveStatus();
        if(userActiveStatus != UserStatus.FROZEN.type){
            String uToken = UUID.randomUUID().toString();
            redis.set(REDIS_USER_TOKEN+":"+user.getId(),uToken);
            //save the user id and token in the cookie
            setCookie(request,response,"utoken",uToken,COOKIE_MONTH);
            //System.out.println("\n\n\n\ndone1\n\n\n");
            setCookie(request,response,"uid",user.getId(),COOKIE_MONTH);
            //System.out.println("\n\n\none2\n\n\n");
        }

        //user should delete the redis valid code
        redis.del(MOBILE_SMSCODE+":"+mobile);

        return GraceJSONResult.ok(userActiveStatus);

        //return null;
    }

    //get the info from the BO
}
