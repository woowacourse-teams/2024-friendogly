package com.happy.friendogly.data.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.happy.friendogly.data.model.ClubDto
import com.happy.friendogly.data.model.SearchClubPageInfoDto
import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.model.SearchClubPageInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun SearchClubPageInfo.toData(): SearchClubPageInfoDto {
    return SearchClubPageInfoDto(
        pageSize = pageSize,
        lastFoundId = lastFoundId.toString(),
        lastFoundCreatedAt = lastFoundCreatedAt.toString(),
    )
}

fun Flow<PagingData<ClubDto>>.toDomain(): Flow<PagingData<Club>> {
    return this.map { pagingData ->
        pagingData.map { it.toDomain() }
    }
}
