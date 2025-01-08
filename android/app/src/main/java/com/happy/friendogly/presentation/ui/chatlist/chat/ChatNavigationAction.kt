package com.happy.friendogly.presentation.ui.chatlist.chat

interface ChatNavigationAction {
    fun navigateToMemberProfile(memberId: Long)

    fun navigateToClub(clubId: Long)

    fun leaveChat()
}
