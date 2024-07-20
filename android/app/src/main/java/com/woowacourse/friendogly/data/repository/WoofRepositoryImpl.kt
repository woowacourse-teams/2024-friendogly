package com.woowacourse.friendogly.data.repository

import com.naver.maps.geometry.LatLng
import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.domain.model.FootPrint
import com.woowacourse.friendogly.domain.model.LandMark
import com.woowacourse.friendogly.domain.repository.WoofRepository
import com.woowacourse.friendogly.remote.source.WoofDataSource

class WoofRepositoryImpl(private val remoteWoofDataSource: WoofDataSource) : WoofRepository {
    override suspend fun getNearFootPrints(latLng: LatLng): Result<List<FootPrint>> {
        return remoteWoofDataSource.getNearFootPrints(latLng).mapCatching { footPrintDtos ->
            footPrintDtos.toDomain()
        }
    }

    override suspend fun getLandMarks(): Result<List<LandMark>> {
        return remoteWoofDataSource.getLandMarks().mapCatching { landMarks ->
            landMarks.toDomain()
        }
    }
}
