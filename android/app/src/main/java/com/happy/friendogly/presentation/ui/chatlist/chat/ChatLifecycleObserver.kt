package com.happy.friendogly.presentation.ui.chatlist.chat

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ChatLifecycleObserver private constructor() : DefaultLifecycleObserver {
    private var state: State = State.Background

    val isForeground
        get() = state == State.Foreground

    val isBackground: Boolean
        get() = state == State.Background

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        state = State.Foreground
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        state = State.Background
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        state = State.Background
    }

    enum class State {
        Foreground,
        Background,
    }

    companion object {
        @Volatile
        private var instance: ChatLifecycleObserver? = null

        fun getInstance(): ChatLifecycleObserver =
            instance ?: synchronized(this) {
                val newInstance = ChatLifecycleObserver()
                instance = newInstance
                newInstance
            }
    }
}
