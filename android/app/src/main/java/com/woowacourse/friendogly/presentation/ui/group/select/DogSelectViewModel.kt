package com.woowacourse.friendogly.presentation.ui.group.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DogSelectViewModel : BaseViewModel(), DogSelectActionHandler {
    private val _dogs: MutableLiveData<List<DogSelectUiModel>> = MutableLiveData()
    val dogs: LiveData<List<DogSelectUiModel>> get() = _dogs

    private val selectedDogs: MutableList<DogSelectUiModel> = mutableListOf()

    private var filters: List<GroupFilter> = listOf()

    private val _dogSelectEvent: MutableLiveData<Event<DogSelectEvent>> = MutableLiveData()
    val dogSelectEvent: LiveData<Event<DogSelectEvent>> get() = _dogSelectEvent

    init {
        loadMyDogs()
    }

    fun loadFilters(filters: List<GroupFilter>) {
        this.filters = filters
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
                        gender = GroupFilter.GenderFilter.Female,
                        size = GroupFilter.SizeFilter.BigDog,
                    ),
                    DogSelectUiModel(
                        id = 0L,
                        profileImage = "",
                        name = "강아지 2",
                        gender = GroupFilter.GenderFilter.Female,
                        size = GroupFilter.SizeFilter.BigDog,
                    ),
                    DogSelectUiModel(
                        id = 0L,
                        profileImage = "",
                        name = "강아지 3",
                        gender = GroupFilter.GenderFilter.Male,
                        size = GroupFilter.SizeFilter.MediumDog,
                    ),
                    DogSelectUiModel(
                        id = 0L,
                        profileImage = "",
                        name = "강아지 4",
                        gender = GroupFilter.GenderFilter.Male,
                        size = GroupFilter.SizeFilter.SmallDog,
                    ),
                    DogSelectUiModel(
                        id = 0L,
                        profileImage = "",
                        name = "강아지 5",
                        gender = GroupFilter.GenderFilter.NeutralizingFemale,
                        size = GroupFilter.SizeFilter.MediumDog,
                    ),
                )
        }

    override fun selectDog(dogSelectUiModel: DogSelectUiModel) {
        if (selectedDogs.contains(dogSelectUiModel)) {
            removeDog(dogSelectUiModel)
        } else {
            if (isValidDogFilter(dogSelectUiModel)) {
                addDog(dogSelectUiModel)
            } else {
                _dogSelectEvent.emit(DogSelectEvent.PreventSelection(dogSelectUiModel.name))
            }
        }
    }

    private fun isValidDogFilter(dogSelectUiModel: DogSelectUiModel): Boolean {
        return filters.contains(dogSelectUiModel.gender) && filters.contains(dogSelectUiModel.size)
    }

    private fun removeDog(dogSelectUiModel: DogSelectUiModel) {
        dogSelectUiModel.unSelectDog()
        selectedDogs.remove(dogSelectUiModel)
        _dogSelectEvent.emit(DogSelectEvent.SelectDog)
    }

    private fun addDog(dogSelectUiModel: DogSelectUiModel) {
        dogSelectUiModel.selectDog()
        selectedDogs.add(dogSelectUiModel)
        _dogSelectEvent.emit(DogSelectEvent.SelectDog)
    }

    override fun submitDogs() {
        if (selectedDogs.size == 0) return
        _dogSelectEvent.emit(
            DogSelectEvent.SelectDogs(
                dogs = selectedDogs.map { it.id },
            ),
        )
    }

    override fun cancelSelection() {
        _dogSelectEvent.emit(DogSelectEvent.CancelSelection)
    }
}
