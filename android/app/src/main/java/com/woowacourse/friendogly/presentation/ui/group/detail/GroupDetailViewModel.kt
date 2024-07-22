package com.woowacourse.friendogly.presentation.ui.group.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
                    groupPoster = "",
                    isParticipable = true,
                    title = "중형견 모임해요",
                    content = "공지 꼭 읽어주세요",
                    maximumNumberOfPeople = 5,
                    currentNumberOfPeople = 2,
                    groupLocation = "잠실6동",
                    groupLeader = "벼리",
                    groupDate = LocalDateTime.now(),
                    groupReaderImage = "",
                    userProfiles =
                        listOf(
                            GroupDetailProfileUiModel(
                                "땡이",
                                "",
                            ),
                            GroupDetailProfileUiModel(
                                "채드",
                                "",
                            ),
                            GroupDetailProfileUiModel(
                                "벼리",
                                "",
                            ),
                            GroupDetailProfileUiModel(
                                "에디",
                                "",
                            ),
                        ),
                    dogProfiles =
                        listOf(
                            GroupDetailProfileUiModel(
                                "땡이",
                                "",
                            ),
                            GroupDetailProfileUiModel(
                                "채드",
                                "",
                            ),
                            GroupDetailProfileUiModel(
                                "벼리",
                                "",
                            ),
                            GroupDetailProfileUiModel(
                                "에디",
                                "",
                            ),
                            GroupDetailProfileUiModel(
                                "땡이",
                                "",
                            ),
                            GroupDetailProfileUiModel(
                                "채드",
                                "",
                            ),
                            GroupDetailProfileUiModel(
                                "벼리",
                                "",
                            ),
                            GroupDetailProfileUiModel(
                                "에디",
                                "",
                            ),
                        ),
                )
        }

    override fun selectDog() {
        _groupDetailEvent.emit(GroupDetailEvent.OpenDogSelector(group.value?.filters ?: listOf()))
    }

    override fun closeDetail() {
        _groupDetailEvent.emit(GroupDetailEvent.Navigation.NavigateToHome)
    }

    override fun openMenu() {
        _groupDetailEvent.emit(GroupDetailEvent.OpenDetailMenu)
    }

    // TODO: join group api
    fun joinGroup() =
        viewModelScope.launch {
            // TODO : success
            _groupDetailEvent.emit(GroupDetailEvent.Navigation.NavigateToChat)
        }
}
