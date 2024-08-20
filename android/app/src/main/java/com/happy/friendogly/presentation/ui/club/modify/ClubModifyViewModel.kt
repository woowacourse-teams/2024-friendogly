package com.happy.friendogly.presentation.ui.club.modify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.model.ClubState
import com.happy.friendogly.domain.usecase.PatchClubUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.utils.addSourceList
import kotlinx.coroutines.launch

class ClubModifyViewModel(
    private val patchClubUseCase: PatchClubUseCase,
) : BaseViewModel(), ClubModifyActionHandler {
    private val _modifyEvent: MutableLiveData<Event<ClubModifyEvent>> = MutableLiveData()
    val modifyEvent: LiveData<Event<ClubModifyEvent>> get() = _modifyEvent

    private var clubId: Long? = null

    private val _clubState: MutableLiveData<ClubState> = MutableLiveData(null)
    val clubState: LiveData<ClubState> get() = _clubState

    val clubTitle: MutableLiveData<String> = MutableLiveData("")

    val clubContent: MutableLiveData<String> = MutableLiveData("")

    val validModify: MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>()
            .apply {
                addSourceList(
                    clubContent,
                    clubTitle,
                    clubState,
                ) {
                    isValidEditGroup()
                }
            }

    fun initUiModel(clubModifyUiModel: ClubModifyUiModel) {
        clubId = clubModifyUiModel.clubId
        clubTitle.value = clubModifyUiModel.title
        clubContent.value = clubModifyUiModel.content
        updateClubState(clubModifyUiModel.clubState)
    }

    fun updateClubState(state: ClubState) {
        _clubState.value = state
    }

    private fun isValidEditGroup(): Boolean {
        val clubTitleLength = clubTitle.value?.length ?: (MIN_TEXT_LENGTH - 1)
        val clubContentLength = clubContent.value?.length ?: (MIN_TEXT_LENGTH - 1)
        return clubTitleLength in MIN_TEXT_LENGTH..MAX_TITLE_LENGTH &&
            clubContentLength in MIN_TEXT_LENGTH..MAX_CONTENT_LENGTH
    }

    private fun isValidModifyState(): Boolean {
        val currentState = clubState.value ?: ClubState.FULL
        return currentState != ClubState.FULL
    }

    override fun cancelModify() {
        _modifyEvent.emit(ClubModifyEvent.Navigation.NavigatePrev)
    }

    override fun submitModify() {
        submitClubModify()
    }

    private fun submitClubModify() =
        viewModelScope.launch {
            patchClubUseCase(
                clubId = clubId ?: return@launch,
                title = clubTitle.value ?: return@launch,
                content = clubContent.value ?: return@launch,
                state = clubState.value ?: return@launch,
            )
                .onSuccess {
                    _modifyEvent.emit(ClubModifyEvent.Navigation.NavigateSubmit)
                }
                .onFailure {
                    _modifyEvent.emit(ClubModifyEvent.FailModify)
                }
        }

    override fun openSelectState() {
        if (isValidModifyState()) {
            _modifyEvent.emit(ClubModifyEvent.Navigation.NavigateSelectState)
        }
    }

    companion object {
        private const val MIN_TEXT_LENGTH = 1
        private const val MAX_TITLE_LENGTH = 100
        private const val MAX_CONTENT_LENGTH = 1000

        fun factory(patchClubUseCase: PatchClubUseCase): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                ClubModifyViewModel(
                    patchClubUseCase = patchClubUseCase,
                )
            }
        }
    }
}
