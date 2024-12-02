package com.happy.friendogly.infra;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {

    private final FileStorageManager fileStorageManager;

    public ImageController(FileStorageManager fileStorageManager) {
        this.fileStorageManager = fileStorageManager;
    }

    @PostMapping("/test-upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println(file.getName());
        String imageUrl = fileStorageManager.uploadFile(file);
        return ResponseEntity.ok(imageUrl);
    }
}