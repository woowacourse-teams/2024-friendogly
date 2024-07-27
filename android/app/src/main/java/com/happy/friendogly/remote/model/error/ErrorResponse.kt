package com.happy.friendogly.remote.model.error

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val isSuccess: Boolean,
    val data: ErrorData,
)

@Serializable
data class ErrorData(
    val errorCode: String, // TODO 아직 에러 코드 문서화가 안 된 상태이기 때문에 String으로 받음
    val errorMessage: String,
    val detail: List<String>,
)

@Serializable
enum class ErrorCode {
    @SerializedName("DEFAULT_ERROR_CODE")
    DEFAULT_ERROR_CODE,
}
