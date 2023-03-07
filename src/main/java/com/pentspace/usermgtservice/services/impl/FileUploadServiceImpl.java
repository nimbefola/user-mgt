package com.pentspace.usermgtservice.services.impl;

import com.pentspace.usermgtservice.services.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.WebResource;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

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
                    .key(entityId)
                    .build();

            S3Client s3Client = getS3Client();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            GetUrlRequest getUrlRequest = GetUrlRequest.builder().bucket(bucketName).key(entityId).build();
            String url = s3Client.utilities().getUrl(getUrlRequest).toString();
            log.info(" Profile picture URL [{}]", url);
            file.getInputStream().close();
            return url;
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

//    public ResponseEntity<InputStreamResource> getCardImageAsStream(String url, boolean isDownload) {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        ClientResponse clientResponse = streamDocument(url, httpHeaders, isDownload);
//        InputStreamResource inputStreamResource = new InputStreamResource(clientResponse.getEntityInputStream());
//        try{
//            MultivaluedMap<String, String> responseHeaders = clientResponse.getHeaders();
//            LOGGER.debug("responseHeaders: [{}] ", responseHeaders);
//        } catch (Exception ex) {
//            LOGGER.warn(ex.getMessage(), ex);
//            try{inputStreamResource.getInputStream().close();}
//            catch (Exception innerEx) {
//                LOGGER.warn(innerEx.getMessage(), innerEx);
//            }
//            throw ex;
//        }
//        return new ResponseEntity(inputStreamResource, httpHeaders, HttpStatus.OK);
//    }

//    public ClientResponse streamDocument(String url, HttpHeaders httpHeaders, boolean isDownload) {
//        WebResource.Builder builder = client.resource(url).getRequestBuilder();
//
//        builder = setUpTrackignHeaders(builder);
//
//        ClientResponse response = builder.get(ClientResponse.class);
//        httpHeaders.add(HttpHeaders.CONTENT_LENGTH, response.getHeaders().getFirst(HttpHeaders.CONTENT_LENGTH));
//        httpHeaders.add(HttpHeaders.CONTENT_TYPE, response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
//
//        // only add content disposition headers when we want browser to download file to local File Storage
//        // e.g. pass through the filename received from private document service
//        if(isDownload) {
//            httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
//        }
//        return response;
//    }
    S3Client getS3Client(){
        ProfileCredentialsProvider profileCredentialsProvider = ProfileCredentialsProvider.create();
        S3Client s3Client = S3Client.builder()
                .region(clientRegion)
                .credentialsProvider(profileCredentialsProvider)
                .build();
        return s3Client;
    }

}
