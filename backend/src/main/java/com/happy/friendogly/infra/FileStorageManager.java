package com.happy.friendogly.infra;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageManager {

    String uploadFile(MultipartFile file);

    void removeFile(String oldImageUrl);

    String updateFile(String oldFileUrl, MultipartFile newFile, ImageUpdateType fileUpdateType);
}
