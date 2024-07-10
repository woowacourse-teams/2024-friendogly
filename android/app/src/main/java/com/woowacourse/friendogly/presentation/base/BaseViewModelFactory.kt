package com.woowacourse.friendogly.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class BaseViewModelFactory<VM : BaseViewModel>(
    private val creator: (CreationExtras) -> VM,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        if (modelClass.isAssignableFrom(creator(extras).javaClass)) {
            @Suppress("UNCHECKED_CAST")
            return creator(extras) as T
        }
        throw IllegalArgumentException("알 수 없는 ViewModel 클래스 : ${modelClass.name}")
    }
}
