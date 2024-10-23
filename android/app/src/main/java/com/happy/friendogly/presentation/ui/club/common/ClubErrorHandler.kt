package com.happy.friendogly.presentation.ui.club.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit

sealed interface ClubErrorEvent {
    data object InternetError : ClubErrorEvent

    data object FileSizeError : ClubErrorEvent

    data object ServerError : ClubErrorEvent

    data object UnKnownError : ClubErrorEvent

    data object OpenError : ClubErrorEvent

    data object ParticipationError : ClubErrorEvent
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
                        DataError.Network.CLUB_OPEN_EXCEED -> ClubErrorEvent.OpenError
                        DataError.Network.CLUB_PARTICIPATION_EXCEED -> ClubErrorEvent.ParticipationError
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
