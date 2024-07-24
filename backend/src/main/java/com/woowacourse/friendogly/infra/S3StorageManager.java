package com.woowacourse.friendogly.infra;

import com.woowacourse.friendogly.exception.FriendoglyException;
import java.io.File;
import java.io.FileOutputStream;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@Profile("!local")
public class S3StorageManager implements FileStorageManager {

    private String BUCKET_NAME = "techcourse-project-2024";

    private String S3_ENDPOINT = "https://d3obq7hxojfffa.cloudfront.net/";

    private String KEY_PREFIX = "friendogly/";

    private final S3Client s3Client;

    public S3StorageManager() {
        this.s3Client = S3Client.builder()
                .credentialsProvider(
                        InstanceProfileCredentialsProvider.create())
                .region(Region.AP_NORTHEAST_2)
                .build();
    }

    public String uploadFile(MultipartFile file) {
        String key = file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(KEY_PREFIX + key)
                .contentType("image/jpg")
                .build();
        RequestBody requestBody = RequestBody.fromFile(convertMultiPartFileToFile(file));
        s3Client.putObject(putObjectRequest, requestBody);
        return S3_ENDPOINT + key;
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
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
