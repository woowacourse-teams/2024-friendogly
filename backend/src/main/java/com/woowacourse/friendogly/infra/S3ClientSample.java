package com.woowacourse.friendogly.infra;

import com.woowacourse.friendogly.exception.FriendoglyException;
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

    public String uploadFile(String key, MultipartFile file) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("techcourse-project-2024")
                .key("friendogly/"+key)
                .contentType("image/jpg")
                .build();
        RequestBody requestBody = RequestBody.fromFile(convertMultiPartFileToFile(file));
        s3Client.putObject(putObjectRequest, requestBody);
        return "https://d3obq7hxojfffa.cloudfront.net/" + key;
    }

    private File convertMultiPartFileToFile(MultipartFile file)  {
        File tmp = null;
        try {
            tmp = File.createTempFile("tmp", null);
        } catch (Exception e) {
            throw new FriendoglyException("MultipartFile의 임시 파일 생성 중 에러 발생");
        }

        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(file.getBytes());
            fos.flush();
        } catch (Exception e) {
            throw new FriendoglyException("MultipartFile to File 변환 중 오류 발생");
        }
        return tmp;
    }

}
