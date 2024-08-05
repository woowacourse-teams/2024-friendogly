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
        const val POST_FOOTPRINTS_WALK_STATUS = "$BASE_URL/walk-status"
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
        const val GET_PETS = "$BASE_URL/{id}"
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
}
