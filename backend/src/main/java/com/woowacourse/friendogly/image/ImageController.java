package com.woowacourse.friendogly.image;

import com.woowacourse.friendogly.infra.S3ClientSample;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {

    private final S3ClientSample s3ClientSample;

    public ImageController(S3ClientSample s3ClientSample) {
        this.s3ClientSample = s3ClientSample;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println(file);
        String key = file.getOriginalFilename();
        try {
            String imageUrl = s3ClientSample.uploadFile(key, file);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }
}
