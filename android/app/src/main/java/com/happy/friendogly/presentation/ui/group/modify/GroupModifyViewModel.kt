package com.happy.friendogly.presentation.ui.group.modify

import android.graphics.Bitmap
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.utils.toBitmap

class GroupModifyViewModel: BaseViewModel(), GroupModifyActionHandler {
    private val _modifyEvent : MutableLiveData<Event<GroupModifyEvent>> = MutableLiveData()
    val modifyEvent: LiveData<Event<GroupModifyEvent>> get()  =_modifyEvent

    private val _groupPoster: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val groupPoster: LiveData<Bitmap?> get() = _groupPoster

    val groupTitle: MutableLiveData<String> = MutableLiveData("")

    val groupContent: MutableLiveData<String> = MutableLiveData("")

    fun initUiModel(
        posterBitmap: Bitmap?,
        groupModifyUiModel: GroupModifyUiModel
    ){
        _groupPoster.value = posterBitmap
        groupTitle.value = groupModifyUiModel.title
        groupContent.value = groupModifyUiModel.content
    }

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
