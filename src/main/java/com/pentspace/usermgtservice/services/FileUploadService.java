package com.pentspace.usermgtservice.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    String uploadFile(String entityId, MultipartFile multipartFile);
    String readAndConvertImageToBase64Read(String key);
}
