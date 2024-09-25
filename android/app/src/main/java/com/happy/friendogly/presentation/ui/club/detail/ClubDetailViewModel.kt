package com.happy.friendogly.presentation.ui.club.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.GetClubUseCase
import com.happy.friendogly.domain.usecase.PostClubMemberUseCase
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.club.common.mapper.toPresentation
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType
import com.happy.friendogly.presentation.ui.club.modify.ClubModifyUiModel
import com.happy.friendogly.presentation.utils.logParticipateClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubDetailViewModel @Inject constructor(
    private val analyticsHelper: AnalyticsHelper,
    private val getClubUseCase: GetClubUseCase,
    private val postClubMemberUseCase: PostClubMemberUseCase,
) : BaseViewModel(), ClubDetailActionHandler {
    private val _club: MutableLiveData<ClubDetailUiModel> = MutableLiveData()
    val club: LiveData<ClubDetailUiModel> get() = _club

    private val _clubDetailEvent: MutableLiveData<Event<ClubDetailEvent>> = MutableLiveData()
    val clubDetailEvent: LiveData<Event<ClubDetailEvent>> get() = _clubDetailEvent

    fun loadClub(clubId: Long) =
        viewModelScope.launch {
            getClubUseCase(clubId)
                .onSuccess {
                    _club.value = it.toPresentation()
                }
                .onFailure {
                    _clubDetailEvent.emit(ClubDetailEvent.FailLoadDetail)
                }
        }

    override fun confirmParticipation() {
        when (club.value?.clubDetailViewType) {
            ClubDetailViewType.RECRUITMENT -> {
                if (club.value?.isUserPetEmpty != false) return requireRegisterUserPet()
                val filters = club.value?.filters ?: listOf()
                _clubDetailEvent.emit(ClubDetailEvent.OpenDogSelector(filters))
            }

            ClubDetailViewType.MINE, ClubDetailViewType.PARTICIPATED -> {
                val chatRoomId = club.value?.chatRoomId ?: return
                _clubDetailEvent.emit(
                    ClubDetailEvent.Navigation.NavigateToChat(chatRoomId),
                )
            }

            else -> return
        }
    }

    override fun closeDetail() {
        _clubDetailEvent.emit(ClubDetailEvent.Navigation.NavigateToHome)
    }

    override fun openMenu() {
        val detailViewType = club.value?.clubDetailViewType ?: return
        _clubDetailEvent.emit(ClubDetailEvent.OpenDetailMenu(detailViewType))
    }

    fun joinClub(dogs: List<Long>) =
        viewModelScope.launch {
            analyticsHelper.logParticipateClick()
            val clubDetailId = club.value?.clubId ?: return@launch
            postClubMemberUseCase(
                id = clubDetailId,
                participatingPetsId = dogs,
            )
                .onSuccess { clubParticipation ->
                    _clubDetailEvent.emit(
                        ClubDetailEvent.Navigation.NavigateToChat(
                            clubParticipation.chatRoomId,
                        ),
                    )
                }
                .onFailure {
                    _clubDetailEvent.emit(ClubDetailEvent.FailParticipation)
                }
        }

    private fun requireRegisterUserPet() {
        _clubDetailEvent.emit(ClubDetailEvent.Navigation.NavigateToRegisterPet)
    }

    fun makeClubModifyUiModel(): ClubModifyUiModel? {
        return club.value?.toClubModifyUiModel()
    }

    companion object {
        fun factory(
            analyticsHelper: AnalyticsHelper,
            getClubUseCase: GetClubUseCase,
            postClubMemberUseCase: PostClubMemberUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                ClubDetailViewModel(
                    analyticsHelper = analyticsHelper,
                    getClubUseCase = getClubUseCase,
                    postClubMemberUseCase = postClubMemberUseCase,
                )
            }
        }
    }
}
