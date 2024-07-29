package com.happy.friendogly.presentation.ui.group.modify

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit

class GroupModifyViewModel: BaseViewModel(), GroupModifyActionHandler {
    private val _modifyEvent : MutableLiveData<Event<GroupModifyEvent>> = MutableLiveData()
    val modifyEvent: LiveData<Event<GroupModifyEvent>> get()  =_modifyEvent

    private val _groupPoster: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val groupPoster: LiveData<Bitmap?> get() = _groupPoster

    override fun cancelModify() {
        _modifyEvent.emit(GroupModifyEvent.Navigation.NavigatePrev)
    }

    override fun submitModify() {
        // TODO: submit api
        _modifyEvent.emit(GroupModifyEvent.Navigation.NavigateSubmit)
    }

    override fun selectGroupImage() {
        _modifyEvent.emit(GroupModifyEvent.Navigation.NavigateToSelectGroupPoster)
    }

    fun updateGroupPoster(bitmap: Bitmap? = null) {
        _groupPoster.value = bitmap
    }

}
