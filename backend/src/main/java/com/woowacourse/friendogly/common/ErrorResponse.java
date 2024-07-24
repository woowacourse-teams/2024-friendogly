package com.woowacourse.friendogly.common;

import java.util.List;

public record ErrorResponse(ErrorCode errorCode, String errorMessage, List<String> detail) {

}
