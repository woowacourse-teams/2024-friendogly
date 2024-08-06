package com.happy.friendogly.presentation.ui.group.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.GetPetsMineUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import kotlinx.coroutines.launch

class DogSelectViewModel(
    private val getPetsMineUseCase: GetPetsMineUseCase,
) : BaseViewModel(), DogSelectActionHandler {
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

    private fun loadMyDogs() = viewModelScope.launch {
        getPetsMineUseCase()
            .onSuccess {
                _dogs.value = it.map { pet ->
                    pet.toDogSelectUiModel()
                }
            }
            .onFailure {
                _dogSelectEvent.emit(DogSelectEvent.FailLoadDog)
            }
    }

    override fun selectDog(dogSelectUiModel: DogSelectUiModel) {
        if (selectedDogs.contains(dogSelectUiModel)) {
            removeDog(dogSelectUiModel)
        } else {
            applyValidDog(dogSelectUiModel)
        }
    }

    private fun applyValidDog(dogSelectUiModel: DogSelectUiModel) {
        if (isValidDogFilter(dogSelectUiModel)) {
            addDog(dogSelectUiModel)
        } else {
            _dogSelectEvent.emit(DogSelectEvent.PreventSelection(dogSelectUiModel.name))
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

    companion object {
        fun factory(getPetsMineUseCase: GetPetsMineUseCase): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                DogSelectViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                )
            }
        }
    }
}
