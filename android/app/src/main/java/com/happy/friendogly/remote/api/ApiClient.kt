package com.happy.friendogly.remote.api

class ApiClient {
    object Auth {
        //        private const val BASE_URL = "/api/auth"
        private const val BASE_URL = "/auth"
        const val POST_KAKAO_LOGIN = "$BASE_URL/kakao/login"
        const val POST_REFRESH = "$BASE_URL/kakao/refresh"
        const val POST_LOGOUT = "$BASE_URL/kakao/logout"
    }

    object PlayGround {
        //        private const val BASE_URL = "/api/playgrounds"
        private const val BASE_URL = "/playgrounds"
        const val POST_PLAYGROUND = BASE_URL
        const val PATCH_PLAYGROUND_ARRIVAL = "$BASE_URL/arrival"
        const val GET_PLAYGROUNDS = "$BASE_URL/locations"
        const val GET_MY_PLAYGROUND = "$BASE_URL/locations/mine"
        const val GET_PLAYGROUND_INFO = "$BASE_URL/{id}"
        const val GET_PLAYGROUND_SUMMARY = "$BASE_URL/{playgroundId}/summary"
        const val POST_PLAYGROUND_JOIN = "$BASE_URL/{playgroundId}/join"
        const val DELETE_PLAYGROUND_LEAVE = "$BASE_URL/leave"
        const val PATCH_PLAYGROUND_MESSAGE = "$BASE_URL/message"
    }

    object Member {
        //        private const val BASE_URL = "/api/members"
        private const val BASE_URL = "/members"
        const val POST_MEMBER = BASE_URL
        const val GET_MEMBER_MINE = "$BASE_URL/mine"
        const val GET_MEMBER = "$BASE_URL/{id}"
        const val PATCH_MEMBER = BASE_URL
        const val DELETE_MEMBER = BASE_URL
    }

    object Pet {
        //        private const val BASE_URL = "/api/pets"
        private const val BASE_URL = "/pets"
        const val GET_PETS_MINE = "$BASE_URL/mine"
        const val POST_PET = BASE_URL
        const val GET_PETS = BASE_URL
        const val GET_PET_EXISTENCE = "$BASE_URL/exists/mine"
        const val PATCH_PETS = "${BASE_URL}/{id}"
    }

    object Club {
        //        private const val BASE_URL = "/api/clubs"
        private const val BASE_URL = "/clubs"
        private const val MEMBER_URL = "/members"
        const val POST_CLUB = BASE_URL
        const val GET_CLUB_SEARCHING = "$BASE_URL/searching"
        const val GET_CLUB = "$BASE_URL/{id}"
        const val POST_CLUB_MEMBER = "$BASE_URL/{clubId}$MEMBER_URL"
        const val DELETE_CLUB_MEMBER = "$BASE_URL/{clubId}$MEMBER_URL"
        const val PATCH_CLUB = "$BASE_URL/{clubId}"
    }

    object MyClub {
        //        private const val BASE_URL = "/api/clubs"
        private const val BASE_URL = "/clubs"
        const val OWNING = "$BASE_URL/owning"
        const val PARTICIPATING = "$BASE_URL/participating"
    }

    object ChatRoom {
        //        private const val BASE_URL = "/api/chat-rooms"
        private const val BASE_URL = "/chat-rooms"
        const val CHAT_LIST = "$BASE_URL/mine/v2"
        const val MEMBERS = "$BASE_URL/{chatRoomId}"
        const val CLUB = "$BASE_URL/{chatRoomId}/club"
        const val LEAVE = "$BASE_URL/leave/{chatRoomId}"
    }

    object ChatMessage {
        //        private const val BASE_URL = "/api/chat-messages"
        private const val BASE_URL = "/chat-messages"
        const val ALL = "$BASE_URL/{chatRoomId}"
        const val TIMES = "$BASE_URL/{chatRoomId}/times"
    }

    object WebSocket {
        fun publishEnter(chatRoomId: Long) = "/publish/chat.$chatRoomId"

        fun publishMessage(chatRoomId: Long) = "/publish/chat.$chatRoomId"

        fun publishLeave(chatRoomId: Long) = "/publish/chat.$chatRoomId"

        fun subscribeChat(chatRoomId: Long) = "/exchange/chat.exchange/room.$chatRoomId"
    }

    object AlarmToken {
        const val DEVICE_TOKEN = "/api/device-tokens"
    }
}
