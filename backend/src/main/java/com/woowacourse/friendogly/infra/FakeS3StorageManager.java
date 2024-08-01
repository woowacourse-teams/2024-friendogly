package com.woowacourse.friendogly.infra;


import com.woowacourse.friendogly.exception.FriendoglyException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Profile("local")
public class FakeS3StorageManager implements FileStorageManager {

    @Override
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FriendoglyException("사진이 업로드되지 않았습니다.");
        }

        return "http://localhost/" + file.getOriginalFilename();
    }
}
