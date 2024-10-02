package com.happy.friendogly.remote.api

class ApiClient {
    object Auth {
        private const val BASE_URL = "/api/auth"
        const val POST_KAKAO_LOGIN = "$BASE_URL/kakao/login"
        const val POST_REFRESH = "$BASE_URL/kakao/refresh"
        const val POST_LOGOUT = "$BASE_URL/kakao/logout"
    }

    object Footprints {
        private const val BASE_URL = "/api/footprints"
        const val POST_FOOTPRINT = BASE_URL
        const val PATCH_FOOTPRINT_RECENT_WALK_STATUS_AUTO = "$BASE_URL/recent/walk-status/auto"
        const val PATCH_FOOTPRINT_RECENT_WALK_STATUS_MANUAL = "$BASE_URL/recent/walk-status/manual"
        const val GET_FOOTPRINTS_NEAR = "$BASE_URL/near"
        const val GET_FOOTPRINT_INFO = "$BASE_URL/{footprintId}"
        const val GET_FOOTPRINT_MINE_LATEST = "$BASE_URL/mine/latest"
        const val DELETE_FOOTPRINT = "$BASE_URL/{footprintId}"
    }

    object Member {
        private const val BASE_URL = "/api/members"
        const val POST_MEMBER = BASE_URL
        const val GET_MEMBER_MINE = "$BASE_URL/mine"
        const val GET_MEMBER = "$BASE_URL/{id}"
        const val PATCH_MEMBER = BASE_URL
        const val DELETE_MEMBER = BASE_URL
    }

    object Pet {
        private const val BASE_URL = "/api/pets"
        const val GET_PETS_MINE = "$BASE_URL/mine"
        const val POST_PET = BASE_URL
        const val GET_PETS = BASE_URL
        const val PATCH_PETS = "${BASE_URL}/{id}"
    }

    object Club {
        private const val BASE_URL = "/api/clubs"
        private const val MEMBER_URL = "/members"
        const val POST_CLUB = BASE_URL
        const val GET_CLUB_SEARCHING = "$BASE_URL/searching"
        const val GET_CLUB = "$BASE_URL/{id}"
        const val POST_CLUB_MEMBER = "$BASE_URL/{clubId}$MEMBER_URL"
        const val DELETE_CLUB_MEMBER = "$BASE_URL/{clubId}$MEMBER_URL"
        const val PATCH_CLUB = "$BASE_URL/{clubId}"
    }

    object MyClub {
        private const val BASE_URL = "/api/clubs"
        const val OWNING = "$BASE_URL/owning"
        const val PARTICIPATING = "$BASE_URL/participating"
    }

    object Chat {
        private const val BASE_URL = "/api/chat-rooms"
        const val CHAT_LIST = "$BASE_URL/mine"
        const val MEMBERS = "$BASE_URL/{chatRoomId}"
        const val CLUB = "$BASE_URL/{chatRoomId}/club"
        const val LEAVE = "$BASE_URL/leave/{chatRoomId}"
    }

    object ChatMessage {
        private const val BASE_URL = "/api/chat-messages/{chatRoomId}"
        const val ALL = "$BASE_URL/{chatRoomId}"
        const val TIMES = "$BASE_URL/{chatRoomId}/times"
    }

    object WebSocket {
        fun publishEnter(chatRoomId: Long) = "/publish/enter/$chatRoomId"

        fun publishMessage(chatRoomId: Long) = "/publish/chat/$chatRoomId"

        fun publishLeave(chatRoomId: Long) = "/publish/leave/$chatRoomId"

        fun subscribeChat(chatRoomId: Long) = "/topic/chat/$chatRoomId"
    }

    object AlarmToken {
        const val DEVICE_TOKEN = "/api/device-tokens"
    }
}
