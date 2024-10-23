package com.happy.friendogly.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.model.ClubAddress
import com.happy.friendogly.domain.model.ClubFilterCondition
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SearchClubPageInfo
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.repository.SearchClubRepository
import com.happy.friendogly.remote.api.SearchingClubService
import com.happy.friendogly.remote.paging.SearchingClubPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchClubRepositoryImpl
    @Inject
    constructor(
        private val service: SearchingClubService,
    ) : SearchClubRepository {
        override suspend fun getSearchingClubs(
            searchClubPageInfo: SearchClubPageInfo,
            filterCondition: ClubFilterCondition,
            address: ClubAddress,
            genderParams: List<Gender>,
            sizeParams: List<SizeType>,
        ): Flow<PagingData<Club>> {
            return Pager(
                config =
                    PagingConfig(
                        pageSize = searchClubPageInfo.pageSize,
                        enablePlaceholders = false,
                    ),
                pagingSourceFactory = {
                    SearchingClubPagingSource(
                        service = service,
                        searchClubPageInfoDto = searchClubPageInfo.toData(),
                        filterCondition = filterCondition.toData(),
                        address = address.toData(),
                        genderParams = genderParams.map { it.toData() },
                        sizeParams = sizeParams.map { it.toData() },
                    )
                },
            ).flow.toDomain()
        }
    }
