package com.woowacourse.friendogly.infra;

import java.io.File;
import java.io.FileOutputStream;
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
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("techcourse-project-2024")
                .key("test_dog12")
                .build();
        RequestBody requestBody = RequestBody.fromFile(convertMultiPartFileToFile(file));
        s3Client.putObject(putObjectRequest,requestBody);
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File tmp = File.createTempFile("/friendogly/", null);
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(file.getBytes());
            fos.flush();
        } catch (IOException e) {
            throw new IOException("파일 변환 중 오류 발생", e);
        }

        return tmp;
    }

}
