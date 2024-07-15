package com.woowacourse.friendogly.presentation.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import java.time.LocalDate

class MyPageViewModel : BaseViewModel(), MyPageActionHandler {
    private val _uiState: MutableLiveData<MyPageUiState> = MutableLiveData(MyPageUiState())
    val uiState: LiveData<MyPageUiState> get() = _uiState

    private val _navigateAction: MutableLiveData<Event<MyPageNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<MyPageNavigationAction>> get() = _navigateAction

    init {
        fetchDummy()
    }

    private fun fetchDummy() {
        val state = _uiState.value ?: return

        _uiState.value =
            state.copy(
                nickname = "손흥민",
                email = "tottenham@gmail.com",
                dogs = dogs,
            )
    }

    fun navigateToDogRegister() {
        _navigateAction.emit(MyPageNavigationAction.NavigateToDogRegister)
    }

    override fun navigateToDogDetail() {
        _navigateAction.emit(MyPageNavigationAction.NavigateToDogDetail)
    }

    companion object {
        val dog =
            Dog(
                name = "땡이",
                description = "강인해요",
                birthDate = LocalDate.now(),
                sizeType = "",
                gender = "",
                isNeutered = true,
                image = "https://github.com/user-attachments/assets/9329234e-e47d-4fc5-b4b5-9f2a827b60b1",
            )
        val dogs =
            listOf<Dog>(
                dog,
                dog.copy(
                    name = "초코",
                    image = "https://github.com/user-attachments/assets/a344d355-8b00-4e08-a33f-08db58010b07",
                ),
                dog.copy(
                    name = "도토리",
                    image = "https://petsstore.co.kr/web/product/big/202401/dc7c18de083f0ab58060b4ec82321028.jpg",
                ),
            )
    }
}
