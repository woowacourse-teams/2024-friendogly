package com.happy.friendogly.domain.repository

import androidx.paging.PagingData
import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.model.ClubAddress
import com.happy.friendogly.domain.model.ClubFilterCondition
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SearchClubPageInfo
import com.happy.friendogly.domain.model.SizeType
import kotlinx.coroutines.flow.Flow

interface SearchClubRepository {
    suspend fun getSearchingClubs(
        searchClubPageInfo: SearchClubPageInfo,
        filterCondition: ClubFilterCondition,
        address: ClubAddress,
        genderParams: List<Gender>,
        sizeParams: List<SizeType>,
    ): Flow<PagingData<Club>>
}
