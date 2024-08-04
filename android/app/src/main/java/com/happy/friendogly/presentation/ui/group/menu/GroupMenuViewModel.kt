package com.happy.friendogly.presentation.ui.group.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.domain.model.GroupDetailViewType

class GroupMenuViewModel : BaseViewModel(), GroupMenuActionHandler {
    private val _groupMenuEvent: MutableLiveData<Event<GroupMenuEvent>> = MutableLiveData()
    val groupMenuEvent: LiveData<Event<GroupMenuEvent>> get() = _groupMenuEvent

    private var _Group_detailViewType: MutableLiveData<GroupDetailViewType> =
        MutableLiveData(GroupDetailViewType.RECRUITMENT)
    val groupDetailViewType: LiveData<GroupDetailViewType> get() = _Group_detailViewType

    fun initDetailViewType(groupDetailViewType: GroupDetailViewType) {
        _Group_detailViewType.value = groupDetailViewType
    }

    // TODO : delete api
    fun withdrawGroup() {
        _groupMenuEvent.emit(GroupMenuEvent.Navigation.NavigateToPrev)
    }

    override fun close() {
        _groupMenuEvent.emit(GroupMenuEvent.CancelSelection)
    }

    override fun selectModify() {
        _groupMenuEvent.emit(GroupMenuEvent.Modify)
    }

    override fun selectDelete() {
        _groupMenuEvent.emit(GroupMenuEvent.Delete)
    }

    override fun selectReport() {
        _groupMenuEvent.emit(GroupMenuEvent.Report)
    }

    override fun selectBlock() {
        _groupMenuEvent.emit(GroupMenuEvent.Block)
    }
}
