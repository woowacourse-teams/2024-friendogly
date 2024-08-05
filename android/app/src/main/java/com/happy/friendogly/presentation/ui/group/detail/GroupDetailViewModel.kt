package com.happy.friendogly.presentation.ui.group.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailViewType
import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailProfileUiModel
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.happy.friendogly.presentation.ui.group.modify.GroupModifyUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

class GroupDetailViewModel : BaseViewModel(), GroupDetailActionHandler {
    private val _group: MutableLiveData<GroupDetailUiModel> = MutableLiveData()
    val group: LiveData<GroupDetailUiModel> get() = _group

    private val _groupDetailEvent: MutableLiveData<Event<GroupDetailEvent>> = MutableLiveData()
    val groupDetailEvent: LiveData<Event<GroupDetailEvent>> get() = _groupDetailEvent

    // TODO: remove dummy
    fun loadGroup(groupId: Long) =
        viewModelScope.launch {
            delay(1000)
            _group.value =
                GroupDetailUiModel(
                    groupId = 0L,
                    filters =
                        listOf(
                            GroupFilter.SizeFilter.SmallDog,
                            GroupFilter.SizeFilter.BigDog,
                            GroupFilter.GenderFilter.Female,
                            GroupFilter.GenderFilter.NeutralizingMale,
                        ),
                    groupPoster = null,
                    groupDetailViewType = GroupDetailViewType.MINE,
                    title = "중형견 모임해요",
                    content = "공지 꼭 읽어주세요",
                    maximumNumberOfPeople = 5,
                    currentNumberOfPeople = 2,
                    groupLocation = "잠실6동",
                    groupLeader = "벼리",
                    groupDate = LocalDateTime.now().toKotlinLocalDateTime(),
                    groupLeaderImage = null,
                    userProfiles =
                        listOf(
                            GroupDetailProfileUiModel(
                                "땡이",
                                null,
                            ),
                            GroupDetailProfileUiModel(
                                "채드",
                                null,
                            ),
                            GroupDetailProfileUiModel(
                                "벼리",
                                null,
                            ),
                            GroupDetailProfileUiModel(
                                "에디",
                                null,
                            ),
                        ),
                    dogProfiles =
                        listOf(
                            GroupDetailProfileUiModel(
                                "땡이",
                                null,
                            ),
                            GroupDetailProfileUiModel(
                                "채드",
                                null,
                            ),
                            GroupDetailProfileUiModel(
                                "벼리",
                                null,
                            ),
                            GroupDetailProfileUiModel(
                                "에디",
                                null,
                            ),
                            GroupDetailProfileUiModel(
                                "땡이",
                                null,
                            ),
                            GroupDetailProfileUiModel(
                                "채드",
                                null,
                            ),
                            GroupDetailProfileUiModel(
                                "벼리",
                                null,
                            ),
                            GroupDetailProfileUiModel(
                                "에디",
                                null,
                            ),
                        ),
                )
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
        _groupDetailEvent.emit(
            GroupDetailEvent.OpenDetailMenu(detailViewType),
        )
    }

    override fun navigateToProfile(id: Long) {
        _groupDetailEvent.emit(GroupDetailEvent.Navigation.NavigateToProfile(id = id))
    }

    // TODO: join group api
    fun joinGroup() =
        viewModelScope.launch {
            // TODO : success
            _groupDetailEvent.emit(GroupDetailEvent.Navigation.NavigateToChat)
        }

    fun makeGroupModifyUiModel(): GroupModifyUiModel? {
        return group.value?.toGroupModifyUiModel()
    }
}
