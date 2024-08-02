package com.happy.friendogly.remote.error

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDataResponse(
    // TODO 아직 에러 코드 문서화가 안 된 상태이기 때문에 String으로 받음
    val errorCode: String,
    val errorMessage: String,
    val detail: List<String>,
)
