package com.happy.friendogly.kakao.source

import android.content.Context
import com.happy.friendogly.data.model.KakaoAccessTokenDto
import com.happy.friendogly.data.source.KakaoLoginDataSource
import com.happy.friendogly.kakao.mapper.toData
import com.happy.friendogly.kakao.model.KakaoAccessTokenResponse
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class KakaoLoginDataSourceImpl : KakaoLoginDataSource {
    /**
     * @param context: Activity context
     */
    override suspend fun login(context: Context): Result<KakaoAccessTokenDto> =
        runCatching {
            suspendCancellableCoroutine { continuation ->
                val callback: (OAuthToken?, Throwable?) -> Unit = { token, throwable ->
                    when {
                        throwable != null -> continuation.resumeWithException(throwable)
                        token != null && continuation.isActive -> {
                            val idToken =
                                token.idToken
                                    ?: throw IllegalArgumentException("kakao login idToken이 없습니다.")
                            val accessToken =
                                KakaoAccessTokenResponse(
                                    accessToken = token.accessToken,
                                    idToken = idToken,
                                )
                            continuation.resume(accessToken.toData())
                        }
                    }
                }

                val userApiClient = UserApiClient.instance
                if (userApiClient.isKakaoTalkLoginAvailable(context)) {
                    userApiClient.loginWithKakaoTalk(context, callback = callback)
                } else {
                    userApiClient.loginWithKakaoAccount(context, callback = callback)
                }
            }
        }
}
