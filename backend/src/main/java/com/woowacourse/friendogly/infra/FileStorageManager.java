package com.woowacourse.friendogly.infra;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageManager {

    public String uploadFile(MultipartFile file);
}
