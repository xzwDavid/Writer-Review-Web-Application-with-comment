package com.imooc.api.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "controller的标题", tags = {"xx功能的controller"})
public interface HelloControllerApi {
    @ApiOperation(value = "hello方法的接口", notes="hello方法的接口", httpMethod = "GET")
    @GetMapping("/hello")
    public Object hello();
}
