package com.woowacourse.friendogly.presentation.ui.group.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.ui.group.model.GroupFilterSelector
import com.woowacourse.friendogly.presentation.ui.group.model.GroupUiModel
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class GroupListViewModel : BaseViewModel(), GroupListActionHandler {
    private val _groups: MutableLiveData<List<GroupUiModel>> = MutableLiveData()
    val groups: LiveData<List<GroupUiModel>> get() = _groups

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
                    GroupUiModel(
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
                    ),
                    GroupUiModel(
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
                    ),
                )
        }
}
