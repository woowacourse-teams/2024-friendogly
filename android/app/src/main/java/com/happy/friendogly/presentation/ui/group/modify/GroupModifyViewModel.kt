package com.happy.friendogly.presentation.ui.group.modify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.group.detail.model.DetailViewType

class GroupModifyViewModel : BaseViewModel(), GroupModifyActionHandler {
    private val _groupModifyEvent: MutableLiveData<Event<GroupModifyEvent>> = MutableLiveData()
    val groupModifyEvent: LiveData<Event<GroupModifyEvent>> get() = _groupModifyEvent

    private var _detailViewType: MutableLiveData<DetailViewType> = MutableLiveData(DetailViewType.RECRUITMENT)
    val detailViewType: LiveData<DetailViewType> get() = _detailViewType

    fun initDetailViewType(detailViewType: DetailViewType) {
        _detailViewType.value = detailViewType
    }

    override fun close() {
        _groupModifyEvent.emit(GroupModifyEvent.CancelSelection)
    }

    override fun selectModify() {
        _groupModifyEvent.emit(GroupModifyEvent.Modify)
    }

    override fun selectDelete() {
        _groupModifyEvent.emit(GroupModifyEvent.Delete)
    }

    override fun selectReport() {
        _groupModifyEvent.emit(GroupModifyEvent.Report)
    }

    override fun selectBlock() {
        _groupModifyEvent.emit(GroupModifyEvent.Block)
    }
}