package com.happy.friendogly.presentation.ui.club.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.usecase.DeleteClubMemberUseCase
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.club.common.ClubErrorHandler
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType
import com.happy.friendogly.presentation.utils.logDeleteMemberClick
import com.happy.friendogly.presentation.utils.logUpdateClubClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubMenuViewModel
    @Inject
    constructor(
        private val analyticsHelper: AnalyticsHelper,
        private val deleteClubMemberUseCase: DeleteClubMemberUseCase,
    ) : BaseViewModel(), ClubMenuActionHandler {
        val clubErrorHandler = ClubErrorHandler()

        private val _clubMenuEvent: MutableLiveData<Event<ClubMenuEvent>> = MutableLiveData()
        val clubMenuEvent: LiveData<Event<ClubMenuEvent>> get() = _clubMenuEvent

        private var _clubDetailViewType: MutableLiveData<ClubDetailViewType> =
            MutableLiveData(ClubDetailViewType.RECRUITMENT)
        val clubDetailViewType: LiveData<ClubDetailViewType> get() = _clubDetailViewType

        fun initDetailViewType(clubDetailViewType: ClubDetailViewType) {
            _clubDetailViewType.value = clubDetailViewType
        }

        fun withdrawClub(clubId: Long) =
            viewModelScope.launch {
                deleteClubMemberUseCase(clubId)
                    .fold(
                        onSuccess = {
                            _clubMenuEvent.emit(ClubMenuEvent.Navigation.NavigateToPrev)
                        },
                        onError = { error ->
                            clubErrorHandler.handle(error)
                        },
                    )
            }

        override fun close() {
            _clubMenuEvent.emit(ClubMenuEvent.CancelSelection)
        }

        override fun selectModify() {
            analyticsHelper.logUpdateClubClick()
            _clubMenuEvent.emit(ClubMenuEvent.Modify)
        }

        override fun selectDelete() {
            analyticsHelper.logDeleteMemberClick()
            _clubMenuEvent.emit(ClubMenuEvent.Delete)
        }

        override fun selectReport() {
            _clubMenuEvent.emit(ClubMenuEvent.Report)
        }

        override fun selectBlock() {
            _clubMenuEvent.emit(ClubMenuEvent.Block)
        }
    }
