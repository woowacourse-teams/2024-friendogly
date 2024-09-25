package com.happy.friendogly.remote.source

import com.happy.friendogly.data.error.ApiExceptionDto.Companion.FileSizeExceedExceptionDto
import com.happy.friendogly.data.model.ImageUpdateTypeDto
import com.happy.friendogly.data.model.MemberDto
import com.happy.friendogly.data.model.RegisterDto
import com.happy.friendogly.data.source.MemberDataSource
import com.happy.friendogly.remote.api.MemberService
import com.happy.friendogly.remote.error.ApiExceptionResponse
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.mapper.toRemote
import com.happy.friendogly.remote.model.request.PatchMemberRequest
import com.happy.friendogly.remote.model.request.PostMembersRequest
import okhttp3.MultipartBody
import javax.inject.Inject

class MemberDataSourceImpl
    @Inject
    constructor(
        private val service: MemberService,
    ) : MemberDataSource {
        override suspend fun postMember(
            name: String,
            accessToken: String,
            file: MultipartBody.Part?,
        ): Result<RegisterDto> {
            val result =
                runCatching {
                    val body = PostMembersRequest(name = name, accessToken = accessToken)
                    service.postMember(body = body, file = file).data.toData()
                }
            return when (val exception = result.exceptionOrNull()) {
                null -> result
                is ApiExceptionResponse -> Result.failure(exception.toData())
                is IllegalStateException -> Result.failure(FileSizeExceedExceptionDto)
                else -> Result.failure(exception)
            }
        }

        override suspend fun getMemberMine(): Result<MemberDto> {
            val result =
                runCatching {
                    service.getMemberMine().data.toData()
                }
            return when (val exception = result.exceptionOrNull()) {
                null -> result
                is ApiExceptionResponse -> Result.failure(exception.toData())
                else -> Result.failure(exception)
            }
        }

        override suspend fun getMember(id: Long): Result<MemberDto> {
            val result =
                runCatching {
                    service.getMember(id = id).data.toData()
                }
            return when (val exception = result.exceptionOrNull()) {
                null -> result
                is ApiExceptionResponse -> Result.failure(exception.toData())
                else -> Result.failure(exception)
            }
        }

        override suspend fun patchMember(
            name: String,
            imageUpdateType: ImageUpdateTypeDto,
            file: MultipartBody.Part?,
        ): Result<MemberDto> {
            val result =
                runCatching {
                    val body =
                        PatchMemberRequest(name = name, imageUpdateType = imageUpdateType.toRemote())
                    service.patchMember(body = body, file = file).data.toData()
                }

            return when (val exception = result.exceptionOrNull()) {
                null -> result
                is ApiExceptionResponse -> Result.failure(exception.toData())
                is IllegalStateException -> Result.failure(FileSizeExceedExceptionDto)
                else -> Result.failure(exception)
            }
        }

        override suspend fun deleteMember(): Result<Unit> {
            val result = runCatching { service.deleteMember() }

            return when (val exception = result.exceptionOrNull()) {
                null -> result
                is ApiExceptionResponse -> Result.failure(exception.toData())
                else -> Result.failure(exception)
            }
        }
    }
