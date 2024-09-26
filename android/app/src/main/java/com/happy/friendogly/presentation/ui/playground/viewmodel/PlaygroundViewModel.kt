package com.happy.friendogly.presentation.ui.playground.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.GetFootprintInfoUseCase
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundActionHandler
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertActions
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundNavigateActions
import com.happy.friendogly.presentation.ui.playground.mapper.toPetDetailInfoPresentation
import com.happy.friendogly.presentation.ui.playground.uimodel.PetDetailInfoUiModel
import com.happy.friendogly.presentation.utils.logPetImageClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaygroundViewModel
    @Inject
    constructor(
        private val analyticsHelper: AnalyticsHelper,
        private val getFootprintInfoUseCase: GetFootprintInfoUseCase,
    ) : ViewModel(), PlaygroundActionHandler {
        private val _petDetailInfo: MutableLiveData<List<PetDetailInfoUiModel>> = MutableLiveData()
        val petDetailInfo: LiveData<List<PetDetailInfoUiModel>> get() = _petDetailInfo

        private val _alertActions: MutableLiveData<Event<PlaygroundAlertActions>> = MutableLiveData()
        val alertActions: LiveData<Event<PlaygroundAlertActions>> get() = _alertActions

        private val _navigateActions: MutableLiveData<Event<PlaygroundNavigateActions>> =
            MutableLiveData()
        val navigateActions: LiveData<Event<PlaygroundNavigateActions>> get() = _navigateActions

        fun loadPetDetailInfo(playgroundId: Long) {
            viewModelScope.launch {
                getFootprintInfoUseCase(playgroundId).onSuccess { footprintInfo ->
                    _petDetailInfo.value = footprintInfo.toPetDetailInfoPresentation()
                }.onFailure {
                    _alertActions.emit(PlaygroundAlertActions.AlertFailToLoadPlaygroundInfoSnackbar)
                }
            }
        }

        override fun clickPetImage(petImageUrl: String) {
            analyticsHelper.logPetImageClicked()
            _navigateActions.emit(PlaygroundNavigateActions.NavigateToPetImage(petImageUrl))
        }
    }
