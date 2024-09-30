package com.happy.friendogly.presentation.ui.club.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.R
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.base.observeEvent

sealed interface ClubErrorEvent {
    data object InternetError : ClubErrorEvent

    data object FileSizeError : ClubErrorEvent

    data object ServerError : ClubErrorEvent

    data object UnKnownError : ClubErrorEvent
}

class ClubErrorHandler {
    private val _error: MutableLiveData<Event<ClubErrorEvent>> = MutableLiveData(null)
    val error: LiveData<Event<ClubErrorEvent>> get() = _error

    fun handle(error: DataError) {
        _error.emit(
            when (error) {
                is DataError.Network -> {
                    when (error) {
                        DataError.Network.NO_INTERNET -> ClubErrorEvent.InternetError
                        DataError.Network.FILE_SIZE_EXCEED -> ClubErrorEvent.FileSizeError
                        DataError.Network.UNKNOWN -> ClubErrorEvent.UnKnownError
                        else -> ClubErrorEvent.ServerError
                    }
                }

                is DataError.Local -> {
                    // TODO : Local Exception
                    ClubErrorEvent.UnKnownError
                }
            },
        )
    }
}

fun ClubErrorHandler.observeClubError(
    owner: LifecycleOwner,
    sendToast: (Int) -> Unit,
    sendSnackBar: (Int) -> Unit,
) {
    this.error.observeEvent(owner) { clubError ->
        when (clubError) {
            ClubErrorEvent.FileSizeError -> sendToast(R.string.file_size_exceed_message)
            ClubErrorEvent.ServerError -> sendToast(R.string.server_error_message)
            ClubErrorEvent.UnKnownError -> sendToast(R.string.default_error_message)
            ClubErrorEvent.InternetError -> sendSnackBar(R.string.no_internet_message)
        }
    }
}
