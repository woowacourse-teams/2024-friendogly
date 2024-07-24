package com.woowacourse.friendogly.infra;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageManager {

    String uploadFile(MultipartFile file);
}
