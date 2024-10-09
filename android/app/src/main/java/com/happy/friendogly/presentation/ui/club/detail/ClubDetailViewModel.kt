package com.happy.friendogly.presentation.ui.club.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.model.ClubState
import com.happy.friendogly.domain.usecase.GetClubUseCase
import com.happy.friendogly.domain.usecase.PatchClubUseCase
import com.happy.friendogly.domain.usecase.PostClubMemberUseCase
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.club.common.ClubErrorHandler
import com.happy.friendogly.presentation.ui.club.common.mapper.toPresentation
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType
import com.happy.friendogly.presentation.ui.club.modify.ClubModifyUiModel
import com.happy.friendogly.presentation.utils.logParticipateClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubDetailViewModel
@Inject
constructor(
    private val analyticsHelper: AnalyticsHelper,
    private val getClubUseCase: GetClubUseCase,
    private val postClubMemberUseCase: PostClubMemberUseCase,
    private val patchClubUseCase: PatchClubUseCase,
) : BaseViewModel(), ClubDetailActionHandler {
    val clubErrorHandler = ClubErrorHandler()

    private val _club: MutableLiveData<ClubDetailUiModel> = MutableLiveData()
    val club: LiveData<ClubDetailUiModel> get() = _club

    private val _clubDetailEvent: MutableLiveData<Event<ClubDetailEvent>> = MutableLiveData()
    val clubDetailEvent: LiveData<Event<ClubDetailEvent>> get() = _clubDetailEvent

    fun loadClub(clubId: Long) =
        viewModelScope.launch {
            getClubUseCase(clubId).fold(
                onSuccess = { club ->
                    _club.value = club.toPresentation()
                },
                onError = { error ->
                    clubErrorHandler.handle(error)
                },
            )
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

    override fun openSelectState() {
        _clubDetailEvent.emit(ClubDetailEvent.Navigation.NavigateSelectState)
    }

    fun joinClub(dogs: List<Long>) =
        viewModelScope.launch {
            analyticsHelper.logParticipateClick()
            val clubDetailId = club.value?.clubId ?: return@launch
            postClubMemberUseCase(
                id = clubDetailId,
                participatingPetsId = dogs,
            ).fold(
                onSuccess = { clubParticipation ->
                    _clubDetailEvent.emit(
                        ClubDetailEvent.Navigation.NavigateToChat(
                            clubParticipation.chatRoomId,
                        ),
                    )
                },
                onError = { error ->
                    clubErrorHandler.handle(error)
                },
            )
        }

    fun submitClubStateModify(newState: ClubState) =
        viewModelScope.launch {
            val club = club.value ?: return@launch
            patchClubUseCase(
                clubId = club.clubId,
                title = club.title,
                content = club.content,
                state = newState
            ).fold(
                onSuccess = {
                    _club.value = club.copy(clubState = newState)
                    _clubDetailEvent.emit(ClubDetailEvent.SaveReLoadState)
                },
                onError = { error ->
                    clubErrorHandler.handle(error)
                }
            )
        }

    private fun requireRegisterUserPet() {
        _clubDetailEvent.emit(ClubDetailEvent.Navigation.NavigateToRegisterPet)
    }

    fun makeClubModifyUiModel(): ClubModifyUiModel? {
        return club.value?.toClubModifyUiModel()
    }
}
