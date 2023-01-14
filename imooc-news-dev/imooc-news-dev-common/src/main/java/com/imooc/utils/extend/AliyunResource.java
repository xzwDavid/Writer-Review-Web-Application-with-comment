package com.imooc.utils.extend;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:aliyun.properties")
@ConfigurationProperties(prefix = "aliyun")
public class AliyunResource {
        private String accessKeyID;
        private String accessKeySecret;

        public String getAccessKeyID(){
           // System.out.println("\n"+"\n"+"xzw"+accessKeyID+"\n"+"\n");
            return accessKeyID;
        }
        public void setAccessKeyID(String accessKeyID){

            this.accessKeyID = accessKeyID;
        }
        public String getAccessKeySecret(){
            return accessKeySecret;
        }
        public void setAccessKeySecret(String accessKeySecret){
            //System.out.println("\n"+"\n"+"hhhhhxzw"+accessKeyID+"\n"+"\n");
            this.accessKeySecret = accessKeySecret;
        }
}
