package com.imooc.exception;

import com.imooc.grace.result.ResponseStatusEnum;

public class MyCustomException extends RuntimeException {
    private ResponseStatusEnum responseStatusEnum;
    public MyCustomException(ResponseStatusEnum responseStatusEnum){
        super("异常状态码为："+responseStatusEnum.status()+";具体异常信息为："
        +responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }
    public ResponseStatusEnum getResponseStatusEnum(){
        return responseStatusEnum;
    }
}
