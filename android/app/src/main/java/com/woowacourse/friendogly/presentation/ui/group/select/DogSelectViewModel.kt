package com.woowacourse.friendogly.presentation.ui.group.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.BaseViewModelFactory
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DogSelectViewModel(
    private val filters: List<GroupFilter>,
) : BaseViewModel(), DogSelectActionHandler {
    private val _dogs: MutableLiveData<List<DogSelectUiModel>> = MutableLiveData()
    val dogs: LiveData<List<DogSelectUiModel>> get() = _dogs

    private val selectedDogs: MutableList<DogSelectUiModel> = mutableListOf()

    private val _dogSelectEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    val dogSelectEvent: LiveData<Event<Unit>> get() = _dogSelectEvent

    init {
        loadMyDogs()
    }

    // TODO: romove sample
    private fun loadMyDogs() =
        viewModelScope.launch {
            delay(1000)
            _dogs.value =
                listOf(
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
                    ),
                    DogSelectUiModel(
                        id = 0L,
                        profileImage = "",
                        name = "강아지 4",
                    ),
                    DogSelectUiModel(
                        id = 0L,
                        profileImage = "",
                        name = "강아지 5",
                    ),
                )
        }

    override fun selectDog(dogSelectUiModel: DogSelectUiModel) {
        if (selectedDogs.contains(dogSelectUiModel)) {
            dogSelectUiModel.unSelectDog()
            selectedDogs.remove(dogSelectUiModel)
        } else {
            dogSelectUiModel.selectDog()
            selectedDogs.add(dogSelectUiModel)
        }
        _dogSelectEvent.emit()
    }

    override fun submitDogs() {
        TODO("Not yet implemented")
    }

    override fun cancelSelection() {
        TODO("Not yet implemented")
    }

    companion object {
        fun factory(filters: List<GroupFilter>): ViewModelProvider.Factory{
            return BaseViewModelFactory { _ ->
                DogSelectViewModel(filters)
            }
        }
    }
}
