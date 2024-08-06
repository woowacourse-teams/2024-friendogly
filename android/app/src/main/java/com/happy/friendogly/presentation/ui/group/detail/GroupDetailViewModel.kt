package com.happy.friendogly.presentation.ui.group.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.mapper.toPresentation
import com.happy.friendogly.domain.usecase.GetClubUseCase
import com.happy.friendogly.domain.usecase.PostClubMemberUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.group.detail.GroupDetailActivity.Companion.FAIL_LOAD_DATA_ID
import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailViewType
import com.happy.friendogly.presentation.ui.group.modify.GroupModifyUiModel
import kotlinx.coroutines.launch

class GroupDetailViewModel(
    private val getClubUseCase: GetClubUseCase,
    private val postClubMemberUseCase: PostClubMemberUseCase,
) : BaseViewModel(), GroupDetailActionHandler {
    private val _group: MutableLiveData<GroupDetailUiModel> = MutableLiveData()
    val group: LiveData<GroupDetailUiModel> get() = _group

    private val _groupDetailEvent: MutableLiveData<Event<GroupDetailEvent>> = MutableLiveData()
    val groupDetailEvent: LiveData<Event<GroupDetailEvent>> get() = _groupDetailEvent

    fun loadGroup(groupId: Long) = viewModelScope.launch {
        getClubUseCase(groupId)
            .onSuccess {
                _group.value = it.toPresentation()
            }
            .onFailure {
                _groupDetailEvent.emit(GroupDetailEvent.FailLoadDetail)
            }
    }

    override fun confirmParticipation() {
        when (group.value?.groupDetailViewType) {
            GroupDetailViewType.RECRUITMENT -> {
                val filters = group.value?.filters ?: listOf()
                _groupDetailEvent.emit(GroupDetailEvent.OpenDogSelector(filters))
            }

            GroupDetailViewType.MINE -> _groupDetailEvent.emit(GroupDetailEvent.Navigation.NavigateToChat)
            else -> return
        }
    }

    override fun closeDetail() {
        _groupDetailEvent.emit(GroupDetailEvent.Navigation.NavigateToHome)
    }

    override fun openMenu() {
        val detailViewType = group.value?.groupDetailViewType ?: return
        _groupDetailEvent.emit(GroupDetailEvent.OpenDetailMenu(detailViewType))
    }

    override fun navigateToProfile(id: Long) {
        _groupDetailEvent.emit(GroupDetailEvent.Navigation.NavigateToProfile(id = id))
    }

    fun joinGroup(dogs: List<Long>) = viewModelScope.launch {
        val groupDetailId = group.value?.groupId ?: return@launch
        postClubMemberUseCase(
            id = groupDetailId,
            participatingPetsId = dogs,
        )
            .onSuccess {
                _groupDetailEvent.emit(GroupDetailEvent.Navigation.NavigateToChat)
            }
            .onFailure {
                _groupDetailEvent.emit(GroupDetailEvent.FailParticipation)
            }
    }

    fun makeGroupModifyUiModel(): GroupModifyUiModel? {
        return group.value?.toGroupModifyUiModel()
    }

    companion object {
        fun factory(
            getClubUseCase: GetClubUseCase,
            postClubMemberUseCase: PostClubMemberUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                GroupDetailViewModel(
                    getClubUseCase = getClubUseCase,
                    postClubMemberUseCase = postClubMemberUseCase,
                )
            }
        }
    }
}
