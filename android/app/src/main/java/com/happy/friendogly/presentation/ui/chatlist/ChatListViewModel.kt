package com.happy.friendogly.presentation.ui.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.happy.friendogly.presentation.ui.chatlist.uimodel.ChatDummy
import com.happy.friendogly.presentation.ui.chatlist.uimodel.ChatListUiModel
import java.time.LocalDateTime

class ChatListViewModel : ViewModel() {
    private val _chats: MutableLiveData<List<ChatListUiModel>> = MutableLiveData()
    val chats: LiveData<List<ChatListUiModel>> get() = _chats

    fun getChats() {
        _chats.value = dummyChats.map { it.toUiModel() }
    }

    companion object {
        private val dummyChats =
            listOf(
                ChatDummy(
                    "방가방가",
                    "다들 언제 모이시는게 편하세요?",
                    3,
                    7,
                    LocalDateTime.of(2024, 7, 2, 14, 12, 0),
                    "https://www.dailysecu.com/news/photo/202" +
                        "104/123449_145665_1147.png",
                ),
                ChatDummy(
                    "잠실 강아지 정모",
                    "땡이는.... 소형견인 건가요...?",
                    5,
                    32,
                    LocalDateTime.of(2024, 7, 17, 9, 0, 12),
                    "https://img.khan.co.kr/news/2024/03/23/news-" +
                        "p.v1.20240323.c159a4cab6f64473adf462d873e01e43_P1.jpg",
                ),
                ChatDummy(
                    "때래때떙",
                    "저 돼지껍데기 먹고싶어요",
                    12,
                    1000,
                    LocalDateTime.now(),
                    "https://img1.daumcdn.net/thumb/R1280x0.fjpg/?fname=http:" +
                        "//t1.daumcdn.net/brunch/service/user/32E9/image/BA2Qyx3O2oTyEOsXe2ZtE8cRqGk.JPG",
                ),
            ).sortedByDescending { it.dateTime }
    }
}
