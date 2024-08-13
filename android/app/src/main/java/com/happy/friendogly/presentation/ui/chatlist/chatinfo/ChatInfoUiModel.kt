package com.happy.friendogly.presentation.ui.chatlist.chatinfo

data class ChatInfoUiModel(
    val dogSize: List<DogSize>,
    val dogGender: List<DogGender>,
)

data class JoinPeople(
    val nickName: String,
    val isMe: Boolean,
    val isLeader: Boolean,
    val profileUrl: String,
    val memberId: Long,
)

enum class DogSize {
    SMALL,
    MEDIUM,
    LARGE,
}

enum class DogGender {
    MALE,
    FEMALE,
    MALE_NEUTERED,
    FEMALE_NEUTERED,
}
