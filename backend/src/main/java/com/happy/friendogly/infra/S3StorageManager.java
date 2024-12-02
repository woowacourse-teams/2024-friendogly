package com.happy.friendogly.infra;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.util.StringUtils.getFilenameExtension;

import com.happy.friendogly.common.ErrorCode;
import com.happy.friendogly.exception.FriendoglyException;
import io.micrometer.common.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Component
@Profile("!local")
public class S3StorageManager implements FileStorageManager {

    private static final String IMAGE_MIME_TYPE_PREFIX = "image/";
    private static final int FILE_SIZE_LIMIT = 5;
    private static final int MB = 1_048_576;

    @Value("${aws.s3.bucket-name}")
    private String BUCKET_NAME;

    @Value("${aws.s3.server.endpoint}")
    private String S3_ENDPOINT;

    @Value("${aws.s3.server.key-prefix}")
    private String KEY_PREFIX;

    private final S3Client s3Client;

    public S3StorageManager() {
        this.s3Client = S3Client.builder()
                .credentialsProvider(
                        InstanceProfileCredentialsProvider.create())
                .region(Region.AP_NORTHEAST_2)
                .build();
    }

    @Override
    public String uploadFile(MultipartFile file) {
        validateNullOrEmpty(file);
        validateFileSize(file);

        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        validateImageType(contentType);

        String newFilename = generateRandomFileName(fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(KEY_PREFIX + newFilename)
                .contentType(contentType)
                .build();

        RequestBody requestBody = RequestBody.fromFile(convertMultiPartFileToFile(file));

        try {
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (SdkException e) {
            throw new FriendoglyException(e.getMessage(), INTERNAL_SERVER_ERROR);
        }

        return S3_ENDPOINT + newFilename;
    }

    private void validateNullOrEmpty(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FriendoglyException("이미지 파일은 비어 있을 수 없습니다.");
        }
    }

    private void validateFileSize(MultipartFile file) {
        if (file.getSize() > FILE_SIZE_LIMIT * MB) {
            throw new FriendoglyException(
                    String.format("%dMB 미만의 사진만 업로드 가능합니다.", FILE_SIZE_LIMIT),
                    ErrorCode.FILE_SIZE_EXCEED,
                    BAD_REQUEST
            );
        }
    }

    private void validateImageType(String contentType) {
        if (StringUtils.isBlank(contentType)) {
            throw new FriendoglyException("업로드한 파일의 Content-Type을 알 수 없습니다.");
        }

        if (!contentType.startsWith(IMAGE_MIME_TYPE_PREFIX)) {
            throw new FriendoglyException(
                    String.format("이미지 파일만 업로드할 수 있습니다. %s는 이미지 Content-Type이 아닙니다.", contentType));
        }
    }

    private String generateRandomFileName(String fileName) {
        String fileExtension = getFilenameExtension(fileName);

        if (fileExtension == null) {
            return UUID.randomUUID().toString();
        }

        return UUID.randomUUID() + "." + fileExtension;
    }

    private File convertMultiPartFileToFile(MultipartFile multipartFile) {
        try {
            File file = File.createTempFile("tmp", null);
            multipartFile.transferTo(file);
            return file;
        } catch (IllegalStateException | IOException e) {
            throw new FriendoglyException("MultipartFile의 임시 파일 생성 중 에러 발생", INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void removeFile(String oldImageUrl) {
        if (StringUtils.isBlank(oldImageUrl) || !oldImageUrl.startsWith(S3_ENDPOINT)) {
            return;
        }

        String fileName = oldImageUrl.substring(oldImageUrl.lastIndexOf("/") + 1);
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(KEY_PREFIX + fileName)
                    .build());
        } catch (SdkException exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    @Override
    public String updateFile(String oldFileUrl, MultipartFile newFile, ImageUpdateType fileUpdateType) {
        if (fileUpdateType == ImageUpdateType.UPDATE) {
            removeFile(oldFileUrl);
            return uploadFile(newFile);
        }

        if (fileUpdateType == ImageUpdateType.NOT_UPDATE) {
            return oldFileUrl;
        }

        if (fileUpdateType == ImageUpdateType.DELETE) {
            removeFile(oldFileUrl);
            return "";
        }

        throw new FriendoglyException("ImageUpdateType이 올바르지 않습니다.");
    }
}
