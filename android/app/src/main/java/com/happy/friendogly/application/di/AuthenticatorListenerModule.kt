package com.happy.friendogly.application.di

import com.happy.friendogly.remote.api.AuthenticationListener
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticatorListenerModule {
    @Binds
    @Singleton
    abstract fun bindsAuthenticatorListener(listenerImpl: AuthenticationListenerImpl): AuthenticationListener
}
