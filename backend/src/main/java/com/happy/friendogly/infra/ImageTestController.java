package com.happy.friendogly.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Profile("dev")
@RestController
@RequiredArgsConstructor
public class ImageTestController {

    private final FileStorageManager fileStorageManager;

    @PostMapping("/upload-image-test")
    public String upload(@RequestParam("file") MultipartFile file) {
        return fileStorageManager.uploadFile(file);
    }
}
