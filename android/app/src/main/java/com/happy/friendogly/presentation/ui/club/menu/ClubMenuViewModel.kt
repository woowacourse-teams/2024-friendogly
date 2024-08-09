package com.happy.friendogly.presentation.ui.club.menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.DeleteClubMemberUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType
import kotlinx.coroutines.launch

class ClubMenuViewModel(
    private val deleteClubMemberUseCase: DeleteClubMemberUseCase,
) : BaseViewModel(), ClubMenuActionHandler {
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
                .onSuccess {
                    _clubMenuEvent.emit(ClubMenuEvent.Navigation.NavigateToPrev)
                }
                .onFailure {
                    Log.d("sdlfjfsdl", it.message.toString())
                    _clubMenuEvent.emit(ClubMenuEvent.FailDelete)
                }
        }

    override fun close() {
        _clubMenuEvent.emit(ClubMenuEvent.CancelSelection)
    }

    override fun selectModify() {
        _clubMenuEvent.emit(ClubMenuEvent.Modify)
    }

    override fun selectDelete() {
        _clubMenuEvent.emit(ClubMenuEvent.Delete)
    }

    override fun selectReport() {
        _clubMenuEvent.emit(ClubMenuEvent.Report)
    }

    override fun selectBlock() {
        _clubMenuEvent.emit(ClubMenuEvent.Block)
    }

    companion object {
        fun factory(deleteClubMemberUseCase: DeleteClubMemberUseCase): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                ClubMenuViewModel(
                    deleteClubMemberUseCase = deleteClubMemberUseCase,
                )
            }
        }
    }
}
