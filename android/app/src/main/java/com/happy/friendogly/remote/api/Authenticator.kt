package com.happy.friendogly.remote.api

import com.happy.friendogly.local.di.TokenManager
import com.happy.friendogly.remote.interceptor.AuthorizationInterceptor
import com.happy.friendogly.remote.model.response.JwtTokenResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class Authenticator(
    private val refreshService: RefreshService,
    private val tokenManager: TokenManager,
    private val authenticationListener: AuthenticationListener,
) : Authenticator {
    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        val refreshToken =
            runBlocking {
                tokenManager.refreshToken.first()
            }

        if (refreshToken.isBlank()) {
            authenticationListener.onSessionExpired()
            return null
        }
        return runBlocking {
            refresh(refreshToken).fold(
                onSuccess = {
                    tokenManager.saveAccessToken(it.accessToken)
                    tokenManager.saveRefreshToken(it.refreshToken)
                    AuthorizationInterceptor.from(response.request, it.accessToken)
                },
                onFailure = {
                    authenticationListener.onSessionExpired()
                    null
                },
            )
        }
    }

    private fun refresh(refreshToken: String): Result<JwtTokenResponse> =
        runCatching {
            // TODO 아직 리프래시 토큰이 없기 토큰이 만료되거나 서버가 재가동 되면 무조건 터트리도록 했습니다!
            // refreshService.refresh()
            throw IllegalArgumentException()
        }
}
