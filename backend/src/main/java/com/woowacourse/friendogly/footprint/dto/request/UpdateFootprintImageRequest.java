package com.woowacourse.friendogly.footprint.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UpdateFootprintImageRequest(MultipartFile imageFile) {

}
