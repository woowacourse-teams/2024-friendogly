package com.happy.friendogly.infra;


import com.happy.friendogly.exception.FriendoglyException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Profile("local")
public class FakeS3StorageManager implements FileStorageManager {

    @Override
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FriendoglyException("이미지 파일은 비어 있을 수 없습니다.");
        }

        return "http://localhost/" + file.getOriginalFilename();
    }

    @Override
    public void removeFile(String oldImageUrl) {

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
