package com.happy.friendogly.presentation.ui.club.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.club.model.ClubFilterSelector
import com.happy.friendogly.presentation.ui.club.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.model.clubfilter.ParticipationFilter

class ClubFilterViewModel : BaseViewModel(), ClubFilterItemActionHandler, ClubFilterActionHandler {
    private val _clubFilterEvent: MutableLiveData<Event<ClubFilterEvent>> = MutableLiveData()
    val clubFilterEvent: LiveData<Event<ClubFilterEvent>> get() = _clubFilterEvent

    private var participationFilter: ParticipationFilter = ParticipationFilter.ENTIRE

    private val clubFilterSelector = ClubFilterSelector()

    fun updateParticipationFilter(participationFilter: ParticipationFilter) {
        this.participationFilter = participationFilter
    }

    fun initClubFilter(clubFilters: List<ClubFilter>) {
        clubFilterSelector.initClubFilter(clubFilters)
    }

    override fun changeParticipationFilter(filterName: String) {
        val participationFilter = ParticipationFilter.findParticipationFilter(filterName) ?: return
        this.participationFilter = participationFilter
    }

    override fun closeSheet() {
        _clubFilterEvent.emit(ClubFilterEvent.CancelSelection)
    }

    override fun selectFilters() {
        val filters = clubFilterSelector.currentSelectedFilters.value ?: emptyList()
        _clubFilterEvent.emit(ClubFilterEvent.SelectClubFilters(filters))
    }

    override fun selectParticipationFilter() {
        _clubFilterEvent.emit(ClubFilterEvent.SelectParticipation(participationFilter))
    }

    override fun selectClubFilter(
        filterName: String,
        isSelected: Boolean,
    ) {
        val clubFilter = ClubFilter.findClubFilter(filterName) ?: return
        if (isSelected) {
            clubFilterSelector.addClubFilter(clubFilter)
        } else {
            clubFilterSelector.removeClubFilter(clubFilter)
        }
    }
}
