package com.happy.friendogly.presentation.ui.group.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.group.model.GroupFilterSelector
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.happy.friendogly.presentation.ui.group.model.groupfilter.ParticipationFilter

class GroupFilterViewModel : BaseViewModel(), GroupFilterItemActionHandler,GroupFilterActionHandler {
    private val _groupFilterEvent: MutableLiveData<Event<GroupFilterEvent>> = MutableLiveData()
    val groupFilterEvent: LiveData<Event<GroupFilterEvent>> get() = _groupFilterEvent

    private var participationFilter: ParticipationFilter = ParticipationFilter.ENTIRE

    val groupFilter =
        GroupFilterSelector(groupList = listOf())

    fun initParticipationFilter(participationFilter: ParticipationFilter) {
        this.participationFilter = participationFilter
    }

    fun initGroupFilter(groupFilters: List<GroupFilter>) {
        groupFilters.forEach {
            this.groupFilter.addGroupFilter(it)
        }
    }

    override fun changeParticipationFilter(filterName: String) {
        val participationFilter = ParticipationFilter.findParticipationFilter(filterName) ?: return
        this.participationFilter = participationFilter
    }

    override fun closeSheet() {
        _groupFilterEvent.emit(GroupFilterEvent.CancelSelection)
    }

    override fun selectFilters() {
        val filters = groupFilter.currentSelectedFilters.value ?: listOf()
        _groupFilterEvent.emit(GroupFilterEvent.SelectGroupFilters(filters))
    }

    override fun selectParticipationFilter() {
        _groupFilterEvent.emit(GroupFilterEvent.SelectParticipation(participationFilter))
    }

    override fun selectGroupFilter(filterName: String, isSelected: Boolean) {
        _groupFilterEvent.emit(GroupFilterEvent.SelectParticipation(participationFilter))
    }
}
