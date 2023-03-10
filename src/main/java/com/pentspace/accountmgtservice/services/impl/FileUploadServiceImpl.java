package com.pentspace.accountmgtservice.services.impl;

import com.pentspace.accountmgtservice.services.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {
    Region clientRegion = Region.EU_WEST_2;
    String bucketName = "pentspace-profile-picture";
    @Override
    public String uploadFile(String entityId, MultipartFile file) {
        //   Path path = Paths.get("/Users/a13401668/Desktop/UPLOAD");
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(entityId + "." + FilenameUtils.getExtension(file.getOriginalFilename()))
                    .build();

            S3Client s3Client = getS3Client();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            GetUrlRequest getUrlRequest = GetUrlRequest.builder().bucket(bucketName).key(entityId).build();
            String url = s3Client.utilities().getUrl(getUrlRequest).toString();
            log.info(" Profile picture URL [{}]", url);
            file.getInputStream().close();
            return url + "." + FilenameUtils.getExtension(file.getOriginalFilename()) ;
            //    Files.copy(file.getInputStream(),path.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readAndConvertImageToBase64Read(String key){
        S3Client s3Client = getS3Client();
        try {
            InputStream inputStream = s3Client.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build());
            byte[] sourceBytes = IOUtils.toByteArray(inputStream);
            String base64EncodedCardImage = Base64.getEncoder().encodeToString(sourceBytes);
            return base64EncodedCardImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    S3Client getS3Client(){
        ProfileCredentialsProvider profileCredentialsProvider = ProfileCredentialsProvider.create();
        S3Client s3Client = S3Client.builder()
                .region(clientRegion)
                .credentialsProvider(profileCredentialsProvider)
                .build();
        return s3Client;
    }

}
