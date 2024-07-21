package com.woowacourse.friendogly.remote.api

class ApiClient {
    object Footprint {
        private const val BASE_URL = "/footprints"
        const val POST_FOOTPRINT = BASE_URL
        const val GET_FOOTPRINTS = BASE_URL
        const val GET_FOOTPRINT_MINE_LATEST = "$BASE_URL/mine/latest"
    }

    object Member

    object Pet

    object Club {
        private const val BASE_URL = "/clubs"
        const val POST_CLUB = BASE_URL
        const val DELETE_CLUB = "$BASE_URL{id}"
        const val POST_CLUB_PARTICIPATION = "$BASE_URL/participation"
        const val GET_CLUB_MINE = "$BASE_URL/mine"
    }
}
