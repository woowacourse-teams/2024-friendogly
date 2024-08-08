package com.happy.friendogly.presentation.ui.club.modify

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit

class ClubModifyViewModel : BaseViewModel(), ClubModifyActionHandler {
    private val _modifyEvent: MutableLiveData<Event<ClubModifyEvent>> = MutableLiveData()
    val modifyEvent: LiveData<Event<ClubModifyEvent>> get() = _modifyEvent

    private val _clubPoster: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val clubPoster: LiveData<Bitmap?> get() = _clubPoster

    val clubTitle: MutableLiveData<String> = MutableLiveData("")

    val clubContent: MutableLiveData<String> = MutableLiveData("")

    fun initUiModel(
        posterBitmap: Bitmap?,
        clubModifyUiModel: ClubModifyUiModel,
    ) {
        _clubPoster.value = posterBitmap
        clubTitle.value = clubModifyUiModel.title
        clubContent.value = clubModifyUiModel.content
    }

    override fun cancelModify() {
        _modifyEvent.emit(ClubModifyEvent.Navigation.NavigatePrev)
    }

    override fun submitModify() {
        // TODO: submit api
        _modifyEvent.emit(ClubModifyEvent.Navigation.NavigateSubmit)
    }

    override fun selectClubImage() {
        _modifyEvent.emit(ClubModifyEvent.Navigation.NavigateToSelectClubPoster)
    }

    fun updateClubPoster(bitmap: Bitmap? = null) {
        _clubPoster.value = bitmap
    }
}
