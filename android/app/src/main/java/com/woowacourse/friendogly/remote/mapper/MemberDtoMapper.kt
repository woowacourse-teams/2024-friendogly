package com.woowacourse.friendogly.remote.mapper

import android.util.Log
import com.woowacourse.friendogly.data.model.MemberDto
import com.woowacourse.friendogly.remote.model.response.MemberResponse

fun MemberResponse.toData(): MemberDto {
    Log.d("ttt this", this.toString())
    return MemberDto(
        id = id,
    )
}
