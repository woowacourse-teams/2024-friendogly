package com.woowacourse.friendogly.presentation.ui.chatlist.chatinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.woowacourse.friendogly.presentation.base.BaseViewModel

class ChatInfoViewModel : BaseViewModel() {

    private val _chatInfo: MutableLiveData<ChatInfoUiModel> = MutableLiveData()
    val chatInfo: LiveData<ChatInfoUiModel> get() = _chatInfo

    fun getChatInfo() {
        _chatInfo.value = dummyChatInfo
    }


    companion object {
        private val dummyChatInfo = ChatInfoUiModel(
            dogSize = listOf(DogSize.SMALL),
            dogGender = listOf(DogGender.FEMALE),
            people = listOf(
                JoinPeople(
                    "김유연", true, true, "https://i.ytimg.com/vi/eEWrbcm6rHI/maxresdefault.jpg"
                ),
                JoinPeople(
                    "김재중",
                    false,
                    false,
                    "https://dimg.donga.com/wps/NEWS/IMAGE/2021/12/13/110760431.2.jpg"
                )
            )
        )
    }
}
