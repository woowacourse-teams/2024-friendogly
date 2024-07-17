package com.woowacourse.friendogly.presentation.ui.group.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DogSelectViewModel : BaseViewModel(), DogSelectActionHandler {
    private val _dogs: MutableLiveData<List<DogSelectUiModel>> = MutableLiveData()
    val dogs: LiveData<List<DogSelectUiModel>> get() = _dogs

    private val selectedDogs: MutableList<DogSelectUiModel> = mutableListOf()

    init {
        loadMyDogs()
    }

    //TODO: romove sample
    private fun loadMyDogs() = viewModelScope.launch{
        delay(1000)
        _dogs.value = listOf(
            DogSelectUiModel(
                id = 0L,
                profileImage = "",
                name = "강아지 1",
            ),
            DogSelectUiModel(
                id = 0L,
                profileImage = "",
                name = "강아지 2",
            ),
            DogSelectUiModel(
                id = 0L,
                profileImage = "",
                name = "강아지 3",
            )
        )
    }

    override fun choiceDog(dogSelectUiModel: DogSelectUiModel) {
        if (selectedDogs.contains(dogSelectUiModel)){
            dogSelectUiModel.unSelectDog()
            selectedDogs.remove(dogSelectUiModel)
        } else {
            dogSelectUiModel.selectDog()
            selectedDogs.add(dogSelectUiModel)
        }
    }

}
