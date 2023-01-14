package com.imooc.files.resource;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:file-${spring.profiles.active}.properties")
@ConfigurationProperties("file")
public class FileResource {
    private String  host;
    private String endpoint;

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    private String bucketName;
    private String objectName;

    public void setOssHost(String ossHost) {
        this.ossHost = ossHost;
    }

    public String getOssHost() {
        return ossHost;
    }

    private String ossHost;
    public String getObjectName() {
        return objectName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getHost(){
        return host;
    }
    public void setHost(String host){
        this.host = host;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
