package com.happy.friendogly.presentation.ui.club.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.GetClubUseCase
import com.happy.friendogly.domain.usecase.PostClubMemberUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType
import com.happy.friendogly.presentation.ui.club.mapper.toPresentation
import com.happy.friendogly.presentation.ui.club.modify.ClubModifyUiModel
import kotlinx.coroutines.launch

class ClubDetailViewModel(
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
                val filters = club.value?.filters ?: listOf()
                _clubDetailEvent.emit(ClubDetailEvent.OpenDogSelector(filters))
            }

            ClubDetailViewType.MINE -> _clubDetailEvent.emit(ClubDetailEvent.Navigation.NavigateToChat)
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

    override fun navigateToProfile(id: Long) {
        _clubDetailEvent.emit(ClubDetailEvent.Navigation.NavigateToProfile(id = id))
    }

    fun joinClub(dogs: List<Long>) =
        viewModelScope.launch {
            val clubDetailId = club.value?.clubId ?: return@launch
            postClubMemberUseCase(
                id = clubDetailId,
                participatingPetsId = dogs,
            )
                .onSuccess {
                    _clubDetailEvent.emit(ClubDetailEvent.Navigation.NavigateToChat)
                }
                .onFailure {
                    _clubDetailEvent.emit(ClubDetailEvent.FailParticipation)
                }
        }

    fun makeClubModifyUiModel(): ClubModifyUiModel? {
        return club.value?.toClubModifyUiModel()
    }

    companion object {
        fun factory(
            getClubUseCase: GetClubUseCase,
            postClubMemberUseCase: PostClubMemberUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                ClubDetailViewModel(
                    getClubUseCase = getClubUseCase,
                    postClubMemberUseCase = postClubMemberUseCase,
                )
            }
        }
    }
}
