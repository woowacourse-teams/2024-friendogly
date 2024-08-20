package com.happy.friendogly.remote.api

class ApiClient {
    object Auth {
        private const val BASE_URL = "/auth"
        const val POST_KAKAO_LOGIN = "$BASE_URL/kakao/login"
        const val POST_REFRESH = "/$BASE_URL/kakao/refresh"
    }

    object Footprints {
        private const val BASE_URL = "/footprints"
        const val POST_FOOTPRINT = BASE_URL
        const val PATCH_FOOTPRINT_WALK_STATUS = "$BASE_URL/walk-status"
        const val GET_FOOTPRINTS_NEAR = "$BASE_URL/near"
        const val GET_FOOTPRINT_INFO = "$BASE_URL/{footprintId}"
        const val GET_FOOTPRINT_MINE_LATEST = "$BASE_URL/mine/latest"
    }

    object Member {
        private const val BASE_URL = "/members"
        const val POST_MEMBER = BASE_URL
        const val GET_MEMBER_MINE = "$BASE_URL/mine"
        const val GET_MEMBER = "$BASE_URL/{id}"
    }

    object Pet {
        private const val BASE_URL = "/pets"
        const val GET_PETS_MINE = "$BASE_URL/mine"
        const val POST_PET = BASE_URL
        const val GET_PETS = BASE_URL
    }

    object Club {
        private const val BASE_URL = "/clubs"
        private const val MEMBER_URL = "/members"
        const val POST_CLUB = BASE_URL
        const val GET_CLUB_SEARCHING = "$BASE_URL/searching"
        const val GET_CLUB = "$BASE_URL/{id}"
        const val POST_CLUB_MEMBER = "$BASE_URL/{clubId}$MEMBER_URL"
        const val DELETE_CLUB_MEMBER = "$BASE_URL/{clubId}$MEMBER_URL"
    }

    object MyClub {
        private const val BASE_URL = "/clubs"
        const val OWNING = "$BASE_URL/owning"
        const val PARTICIPATING = "$BASE_URL/participating"
    }

    object Chat {
        private const val BASE_URL = "/chat-rooms"
        const val CHAT_LIST = "$BASE_URL/mine"
        const val MEMBERS = "$BASE_URL/{chatRoomId}"
    }

    object WebSocket {
        fun publishEnter(chatRoomId: Long) = "/publish/enter/$chatRoomId"

        fun publishMessage(chatRoomId: Long) = "/publish/chat/$chatRoomId"

        fun publishLeave(chatRoomId: Long) = "/publish/leave/$chatRoomId"

        fun subscribeChat(chatRoomId: Long) = "/topic/chat/$chatRoomId"
    }

    object AlarmToken {
        const val DEVICE_TOKEN = "/device-tokens"
    }
}
