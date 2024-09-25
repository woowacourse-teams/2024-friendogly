package com.happy.friendogly.application.di

import android.content.Context
import com.happy.friendogly.local.di.TokenManager
import com.happy.friendogly.presentation.ui.register.RegisterActivity
import com.happy.friendogly.remote.api.AuthenticationListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AuthenticationListenerImpl
    @Inject
    constructor(
        @ApplicationContext
        private val context: Context,
        private val tokenManager: TokenManager,
    ) : AuthenticationListener {
        override fun onSessionExpired() {
            runBlocking {
                tokenManager.deleteToken()
            }
            context.startActivity(RegisterActivity.getIntent(context))
        }
    }
