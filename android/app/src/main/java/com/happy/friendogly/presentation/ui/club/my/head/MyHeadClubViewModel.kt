package com.happy.friendogly.presentation.ui.club.my.head

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.usecase.GetMyHeadClubUseCase
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
class MyHeadClubViewModel
    @Inject
    constructor(
        private val getMyHeadClubUseCase: GetMyHeadClubUseCase,
    ) : BaseViewModel(), ClubItemActionHandler {
        val clubErrorHandler = ClubErrorHandler()

        private val _myHeadClubs: MutableLiveData<List<ClubItemUiModel>> = MutableLiveData()
        val myHeadClubs: LiveData<List<ClubItemUiModel>> get() = _myHeadClubs

        private val _myHeadClubUiState: MutableLiveData<MyClubUiState> = MutableLiveData(MyClubUiState.Loading)
        val myHeadClubUiState: LiveData<MyClubUiState> get() = _myHeadClubUiState

        private val _myClubEvent: MutableLiveData<Event<MyClubEvent.Navigation>> = MutableLiveData()
        val myClubEvent: LiveData<Event<MyClubEvent.Navigation>> get() = _myClubEvent

        init {
            loadMyHeadClubs()
        }

        fun loadMyHeadClubs() =
            viewModelScope.launch {
                getMyHeadClubUseCase()
                    .fold(
                        onSuccess = { clubs ->
                            if (clubs.isEmpty()) {
                                _myHeadClubUiState.value = MyClubUiState.NotData
                            } else {
                                _myHeadClubUiState.value = MyClubUiState.Init
                            }
                            _myHeadClubs.value = clubs.toPresentation()
                        },
                        onError = { error ->
                            _myHeadClubUiState.value = MyClubUiState.Error
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
