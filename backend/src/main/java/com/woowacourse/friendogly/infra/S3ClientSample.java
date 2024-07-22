package com.woowacourse.friendogly.infra;

import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class S3ClientSample {
    private final S3Client s3Client;

    S3ClientSample(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadFile(String key, MultipartFile file) throws IOException {
        s3Client.putObject(
                PutObjectRequest
                        .builder()
                        .key(key)
                        .bucket("d3obq7hxojfffa.cloudfront.net")
                        .build()
                , RequestBody.fromFile(file.getResource().getFile()));
    }

}
