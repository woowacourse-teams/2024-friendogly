package com.happy.friendogly.infra;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Profile("local")
public class FakeS3StorageManager implements FileStorageManager {

    @Override
    public String uploadFile(MultipartFile file) {
        return "http://localhost/" + file.getOriginalFilename();
    }
}
