package com.happy.friendogly.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.happy.friendogly.data.model.ClubAddressDto
import com.happy.friendogly.data.model.ClubDto
import com.happy.friendogly.data.model.ClubFilterConditionDto
import com.happy.friendogly.data.model.GenderDto
import com.happy.friendogly.data.model.SearchClubPageInfoDto
import com.happy.friendogly.data.model.SizeTypeDto
import com.happy.friendogly.remote.api.SearchingClubService
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.mapper.toRemote
import com.happy.friendogly.remote.model.response.ClubResponse
import com.happy.friendogly.remote.model.response.SearchingClubResponse

class SearchingClubPagingSource(
    private val service: SearchingClubService,
    private val searchClubPageInfoDto: SearchClubPageInfoDto,
    private val filterCondition: ClubFilterConditionDto,
    private val address: ClubAddressDto,
    private val genderParams: List<GenderDto>,
    private val sizeParams: List<SizeTypeDto>,
) : PagingSource<Int, ClubDto>() {
    private var isLastPage: Boolean = false
    private var recentlyItem: ClubResponse? = null
    private var lastPage = STARTING_PAGE_IDX

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ClubDto> {
        val page = params.key ?: STARTING_PAGE_IDX
        if (lastPage >= page) {
            initPageState()
        }
        lastPage = page
        val prevKey = if (page == STARTING_PAGE_IDX) null else page - PAGE_INDEX_SIZE
        var nextKey = if (isLastPage) null else page + PAGE_INDEX_SIZE

        return runCatching {
            if (isLastPage) {
                lastPageUpdate(false)
                return LoadResult.Page(emptyList(), prevKey, nextKey)
            }

            val results = loadSearchData()
            val lastItem = results.content.lastOrNull()

            if (lastItem == null) {
                nextKey = null
            } else {
                recentlyItem = lastItem
            }

            if (lastItem == null && page == STARTING_PAGE_IDX) {
                return LoadResult.Error(NullPointerException())
            }

            lastPageUpdate(results.isLastPage)

            LoadResult.Page(
                prevKey = prevKey,
                nextKey = nextKey,
                data = results.content.toData(),
            )
        }.getOrElse { e ->
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ClubDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(PAGE_INDEX_SIZE)
                ?: anchorPage?.nextKey?.minus(PAGE_INDEX_SIZE)
        }
    }

    private fun lastPageUpdate(hasLastPage: Boolean) {
        isLastPage = hasLastPage
    }

    private suspend fun loadSearchData(): SearchingClubResponse {
        return service.getSearchingClubs(
            filterCondition = filterCondition.toRemote(),
            province = address.province,
            city = address.city,
            village = address.village,
            genderParams = genderParams.map { it.toRemote().name },
            sizeParams = sizeParams.map { it.toRemote().name },
            pageSize = searchClubPageInfoDto.pageSize.toString(),
            lastFoundCreatedAt =
                recentlyItem?.createdAt?.toString()
                    ?: searchClubPageInfoDto.lastFoundCreatedAt,
            lastFoundId =
                recentlyItem?.id?.toString()
                    ?: searchClubPageInfoDto.lastFoundId,
        ).data
    }

    private fun initPageState() {
        isLastPage = false
        recentlyItem = null
    }

    companion object {
        private const val STARTING_PAGE_IDX = 1
        private const val PAGE_INDEX_SIZE = 1
    }
}
