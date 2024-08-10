package com.happy.friendogly.presentation.ui.club.my

import androidx.lifecycle.ViewModelProvider
import com.happy.friendogly.domain.usecase.GetMyClubUseCase
import com.happy.friendogly.domain.usecase.GetMyHeadClubUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory

class MyClubViewModel(
    private val getMyClubUseCase: GetMyClubUseCase,
    private val getMyHeadClubUseCase: GetMyHeadClubUseCase,
): BaseViewModel() {

    companion object {
        fun factory(
            getMyClubUseCase: GetMyClubUseCase,
            getMyHeadClubUseCase: GetMyHeadClubUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                MyClubViewModel(
                    getMyClubUseCase = getMyClubUseCase,
                    getMyHeadClubUseCase = getMyHeadClubUseCase,
                )
            }
        }
    }
}
