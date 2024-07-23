package com.woowacourse.friendogly.presentation.ui.group.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import com.woowacourse.friendogly.presentation.ui.group.model.GroupFilterSelector
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.ParticipationFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class GroupListViewModel : BaseViewModel(), GroupListActionHandler {
    private val _participationFilter: MutableLiveData<ParticipationFilter> =
        MutableLiveData(ParticipationFilter.POSSIBLE)
    val participationFilter: LiveData<ParticipationFilter> get() = _participationFilter

    private val _groups: MutableLiveData<List<GroupListUiModel>> = MutableLiveData()
    val groups: LiveData<List<GroupListUiModel>> get() = _groups

    private val _groupListEvent: MutableLiveData<Event<GroupListEvent>> = MutableLiveData()
    val groupListEvent: LiveData<Event<GroupListEvent>> get() = _groupListEvent

    val groupFilterSelector = GroupFilterSelector()

    init {
        loadGroups()
    }

    // TODO: remove dummy
    private fun loadGroups() =
        viewModelScope.launch {
            delay(1000)
            _groups.value =
                listOf(
                    GroupListUiModel(
                        groupId = 0L,
                        filters =
                            listOf(
                                GroupFilter.SizeFilter.SmallDog,
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
                        groupWoofs = listOf(),
                        groupReaderImage = "",
                    ),
                    GroupListUiModel(
                        groupId = 0L,
                        filters =
                            listOf(
                                GroupFilter.SizeFilter.SmallDog,
                                GroupFilter.GenderFilter.Female,
                            ),
                        groupPoster = "",
                        isParticipable = true,
                        title = "중형견 모임해요",
                        content = "공지 꼭 읽어주세요",
                        maximumNumberOfPeople = 5,
                        currentNumberOfPeople = 3,
                        groupLocation = "잠실5동",
                        groupLeader = "채드",
                        groupDate = LocalDateTime.of(2024, 7, 2, 14, 12, 0),
                        groupWoofs = listOf(),
                        groupReaderImage = "",
                    ),
                )
        }

    override fun loadGroup(groupId: Long) {
        _groupListEvent.emit(GroupListEvent.OpenGroup(groupId))
    }
}
