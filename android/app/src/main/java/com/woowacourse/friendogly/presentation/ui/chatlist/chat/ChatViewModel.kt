package com.woowacourse.friendogly.presentation.ui.chatlist.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import java.time.LocalDate
import java.time.LocalTime

class ChatViewModel : BaseViewModel() {
    private val _chats: MutableLiveData<List<ChatUiModel>> = MutableLiveData()
    val chats: LiveData<List<ChatUiModel>> get() = _chats

    fun getChats(chatId:Long) {
        _chats.value = dummyChats
    }

    companion object {
        private val dummyChats = listOf(
            ChatUiModel.Date(LocalDate.now()),
            ChatUiModel.ComeOut("벼리", true),
            ChatUiModel.ComeOut("누누", true),
            ChatUiModel.Mine("이거 잘 작동되는거 맞냐", LocalTime.now()),
            ChatUiModel.Other(
                "채드",
                "https://m.segye.com/content/image/2" +
                        "021/07/29/20210729517145.jpg",
                "화면 보이는거 보니깐 잘 작동하는듯 \n 근데 나 긴 텍스트 필요하니깐 아무말이나 쓸게 블라블랄",
                LocalTime.of(22, 1)
            )

        )
    }
}
