package com.happy.friendogly.presentation.ui.club.my.participating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.usecase.GetMyClubUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.club.common.ClubErrorHandler
import com.happy.friendogly.presentation.ui.club.common.ClubItemActionHandler
import com.happy.friendogly.presentation.ui.club.common.mapper.toPresentation
import com.happy.friendogly.presentation.ui.club.common.model.ClubItemUiModel
import com.happy.friendogly.presentation.ui.club.my.MyClubEvent
import com.happy.friendogly.presentation.ui.club.my.MyClubUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyParticipatingClubViewModel
    @Inject
    constructor(
        private val getMyClubUseCase: GetMyClubUseCase,
    ) : BaseViewModel(), ClubItemActionHandler {
        val clubErrorHandler = ClubErrorHandler()

        private val _myClubs: MutableLiveData<List<ClubItemUiModel>> = MutableLiveData()
        val myClubs: LiveData<List<ClubItemUiModel>> get() = _myClubs

        private val _myClubUiState: MutableLiveData<MyClubUiState> = MutableLiveData()
        val myClubUiState: LiveData<MyClubUiState> get() = _myClubUiState

        private val _myClubEvent: MutableLiveData<Event<MyClubEvent.Navigation>> = MutableLiveData()
        val myClubEvent: LiveData<Event<MyClubEvent.Navigation>> get() = _myClubEvent

        init {
            loadMyClubs()
        }

        fun loadMyClubs() =
            viewModelScope.launch {
                getMyClubUseCase()
                    .fold(
                        onSuccess = { clubs ->
                            if (clubs.isEmpty()) {
                                _myClubUiState.value = MyClubUiState.NotData
                            } else {
                                _myClubUiState.value = MyClubUiState.Init
                            }
                            _myClubs.value = clubs.toPresentation()
                        },
                        onError = { error ->
                            _myClubUiState.value = MyClubUiState.Error
                            clubErrorHandler.handle(error)
                        },
                    )
            }

        override fun loadClub(clubId: Long) {
            _myClubEvent.emit(MyClubEvent.Navigation.NavigateToClub(clubId))
        }

        override fun addClub() {
            _myClubEvent.emit(MyClubEvent.Navigation.NavigateToAddClub)
        }
    }
