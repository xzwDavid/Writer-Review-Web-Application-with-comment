package com.imooc.files.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploaderService {

    /**
     * 使用fastdfs上传文件
     */
    public String uploadFdfs(MultipartFile file, String fileExtName) throws Exception;

    /**
     * 使用OSS上传文件
     */
    public String uploadOSS(MultipartFile file,
                            String userId,
                            String fileExtName) throws Exception;

}
