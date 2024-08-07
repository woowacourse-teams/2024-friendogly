package com.happy.friendogly.presentation.ui.group.menu

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
import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailViewType
import kotlinx.coroutines.launch

class GroupMenuViewModel(
    private val deleteClubMemberUseCase: DeleteClubMemberUseCase,
) : BaseViewModel(), GroupMenuActionHandler {
    private val _groupMenuEvent: MutableLiveData<Event<GroupMenuEvent>> = MutableLiveData()
    val groupMenuEvent: LiveData<Event<GroupMenuEvent>> get() = _groupMenuEvent

    private var _groupDetailViewType: MutableLiveData<GroupDetailViewType> =
        MutableLiveData(GroupDetailViewType.RECRUITMENT)
    val groupDetailViewType: LiveData<GroupDetailViewType> get() = _groupDetailViewType

    fun initDetailViewType(groupDetailViewType: GroupDetailViewType) {
        _groupDetailViewType.value = groupDetailViewType
    }

    fun withdrawGroup(groupId: Long) =
        viewModelScope.launch {
            deleteClubMemberUseCase(groupId)
                .onSuccess {
                    _groupMenuEvent.emit(GroupMenuEvent.Navigation.NavigateToPrev)
                }
                .onFailure {
                    _groupMenuEvent.emit(GroupMenuEvent.FailDelete)
                }
        }

    override fun close() {
        _groupMenuEvent.emit(GroupMenuEvent.CancelSelection)
    }

    override fun selectModify() {
        _groupMenuEvent.emit(GroupMenuEvent.Modify)
    }

    override fun selectDelete() {
        _groupMenuEvent.emit(GroupMenuEvent.Delete)
    }

    override fun selectReport() {
        _groupMenuEvent.emit(GroupMenuEvent.Report)
    }

    override fun selectBlock() {
        _groupMenuEvent.emit(GroupMenuEvent.Block)
    }

    companion object {
        fun factory(deleteClubMemberUseCase: DeleteClubMemberUseCase): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                GroupMenuViewModel(
                    deleteClubMemberUseCase = deleteClubMemberUseCase,
                )
            }
        }
    }
}
