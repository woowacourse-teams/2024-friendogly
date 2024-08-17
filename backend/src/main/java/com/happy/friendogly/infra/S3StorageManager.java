package com.happy.friendogly.infra;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.happy.friendogly.common.ErrorCode;
import com.happy.friendogly.exception.FriendoglyException;
import io.micrometer.common.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
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

@Component
@Profile("!local")
public class S3StorageManager implements FileStorageManager {

    private static final int FILE_SIZE_LIMIT = 1;
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
        if (file == null || file.isEmpty()) {
            throw new FriendoglyException("이미지 파일은 비어 있을 수 없습니다.");
        }

        if (file.getSize() > FILE_SIZE_LIMIT * MB) {
            throw new FriendoglyException(
                    String.format("%dMB 미만의 사진만 업로드 가능합니다.", FILE_SIZE_LIMIT),
                    ErrorCode.FILE_SIZE_EXCEED,
                    BAD_REQUEST
            );
//            throw new FriendoglyException(String.format("%dMB 미만의 사진만 업로드 가능합니다.", FILE_SIZE_LIMIT));
        }

        // TODO: 실제 파일명에서 확장자 가져오기
        // TODO: jpg 이외의 이미지 파일도 가져올 수 있도록 수정하기
//        String fileName = file.getOriginalFilename();
        String newFilename = UUID.randomUUID() + ".jpg";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(KEY_PREFIX + newFilename)
                .contentType("image/jpg")
                .build();
        RequestBody requestBody = RequestBody.fromFile(convertMultiPartFileToFile(file));
        try {
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (SdkException e) {
            throw new FriendoglyException("s3전송 과정중에 에러 발생", INTERNAL_SERVER_ERROR);
        }
        return S3_ENDPOINT + newFilename;
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
        if (StringUtils.isBlank(oldImageUrl)) {
            throw new FriendoglyException("삭제할 이미지의 URL을 입력해 주세요.");
        }

        if (!oldImageUrl.startsWith(S3_ENDPOINT)) {
            throw new FriendoglyException(String.format("(%s)은 삭제할 수 없는 이미지 URL입니다.", oldImageUrl));
        }

        String fileName = oldImageUrl.substring(oldImageUrl.lastIndexOf("/") + 1);

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(KEY_PREFIX + fileName)
                    .build());
        } catch (SdkException e) {
            throw new FriendoglyException("알 수 없는 이유로 이미지 파일 삭제에 실패했습니다.", INTERNAL_SERVER_ERROR);
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
