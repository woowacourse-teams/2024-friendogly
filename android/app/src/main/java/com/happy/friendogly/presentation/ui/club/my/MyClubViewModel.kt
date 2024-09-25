package com.happy.friendogly.presentation.ui.club.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.usecase.GetPetsMineUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyClubViewModel
    @Inject
    constructor(
        private val getPetsMineUseCase: GetPetsMineUseCase,
    ) : BaseViewModel() {
        private val _myClubEvent: MutableLiveData<Event<MyClubEvent.AddPet>> = MutableLiveData()
        val myClubEvent: LiveData<Event<MyClubEvent.AddPet>> get() = _myClubEvent

        fun loadPetState() =
            viewModelScope.launch {
                getPetsMineUseCase()
                    .fold(
                        onSuccess = { pets ->
                            if (pets.isEmpty()) {
                                _myClubEvent.emit(MyClubEvent.AddPet.OpenAddPet)
                            } else {
                                _myClubEvent.emit(MyClubEvent.AddPet.OpenAddClub)
                            }
                        },
                        onError = {
                            // TODO 예외처리
                        },
                    )
            }
    }
