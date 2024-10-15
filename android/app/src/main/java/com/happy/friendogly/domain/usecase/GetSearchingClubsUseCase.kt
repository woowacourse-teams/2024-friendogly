package com.happy.friendogly.domain.usecase

import androidx.paging.PagingData
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.model.ClubAddress
import com.happy.friendogly.domain.model.ClubFilterCondition
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SearchClubPageInfo
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.repository.SearchClubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchingClubsUseCase
@Inject
constructor(
    private val repository: SearchClubRepository,
) {
    suspend operator fun invoke(
        searchClubPageInfo: SearchClubPageInfo,
        filterCondition: ClubFilterCondition,
        address: ClubAddress,
        genderParams: List<Gender>,
        sizeParams: List<SizeType>,
    ): Flow<PagingData<Club>> =
        repository.getSearchingClubs(
            searchClubPageInfo = searchClubPageInfo,
            filterCondition = filterCondition,
            address = address,
            genderParams = genderParams,
            sizeParams = sizeParams,
        )
}
