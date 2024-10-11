package com.happy.friendogly.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Profile("dev")
public class ImageUploadController {

    private final FileStorageManager fileStorageManager;

    @PostMapping("/upload")
    public void uploadImage(@RequestParam("file") MultipartFile imageFile) {
        fileStorageManager.uploadFile(imageFile);
    }
}
