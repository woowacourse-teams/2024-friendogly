package com.happy.friendogly.presentation.ui.group.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.group.detail.model.DetailViewType

class GroupMenuViewModel : BaseViewModel(), GroupMenuActionHandler {
    private val _groupMenuEvent: MutableLiveData<Event<GroupMenuEvent>> = MutableLiveData()
    val groupMenuEvent: LiveData<Event<GroupMenuEvent>> get() = _groupMenuEvent

    private var _detailViewType: MutableLiveData<DetailViewType> = MutableLiveData(DetailViewType.RECRUITMENT)
    val detailViewType: LiveData<DetailViewType> get() = _detailViewType

    fun initDetailViewType(detailViewType: DetailViewType) {
        _detailViewType.value = detailViewType
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
