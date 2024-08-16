package com.happy.friendogly.presentation.ui.club.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.usecase.GetPetsMineUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import kotlinx.coroutines.launch

class PetSelectViewModel(
    private val getPetsMineUseCase: GetPetsMineUseCase,
) : BaseViewModel(), PetSelectActionHandler {
    private val _pets: MutableLiveData<List<PetSelectUiModel>> = MutableLiveData()
    val pets: LiveData<List<PetSelectUiModel>> get() = _pets

    private val selectedPets: MutableList<PetSelectUiModel> = mutableListOf()

    private var filters: List<ClubFilter> = listOf()

    private val _petSelectEvent: MutableLiveData<Event<PetSelectEvent>> = MutableLiveData()
    val petSelectEvent: LiveData<Event<PetSelectEvent>> get() = _petSelectEvent

    init {
        loadMyPets()
    }

    fun loadFilters(filters: List<ClubFilter>) {
        this.filters = filters
    }

    private fun loadMyPets() =
        launch {
            getPetsMineUseCase().fold(
                onSuccess = { pets ->
                    _pets.value =
                        pets.map { pet ->
                            pet.toPetSelectUiModel()
                        }
                },
                onError = {
                    _petSelectEvent.emit(PetSelectEvent.FailLoadPet)
                },
            )
        }

    override fun selectPet(petSelectUiModel: PetSelectUiModel) {
        if (selectedPets.contains(petSelectUiModel)) {
            removeDog(petSelectUiModel)
        } else {
            applyValidDog(petSelectUiModel)
        }
    }

    private fun applyValidDog(petSelectUiModel: PetSelectUiModel) {
        if (isValidDogFilter(petSelectUiModel)) {
            addDog(petSelectUiModel)
        } else {
            _petSelectEvent.emit(PetSelectEvent.PreventSelection(petSelectUiModel.name))
        }
    }

    private fun isValidDogFilter(petSelectUiModel: PetSelectUiModel): Boolean {
        return filters.contains(petSelectUiModel.gender) && filters.contains(petSelectUiModel.size)
    }

    private fun removeDog(petSelectUiModel: PetSelectUiModel) {
        petSelectUiModel.unSelectDog()
        selectedPets.remove(petSelectUiModel)
        _petSelectEvent.emit(PetSelectEvent.SelectPet)
    }

    private fun addDog(petSelectUiModel: PetSelectUiModel) {
        petSelectUiModel.selectDog()
        selectedPets.add(petSelectUiModel)
        _petSelectEvent.emit(PetSelectEvent.SelectPet)
    }

    override fun submitDogs() {
        if (selectedPets.size == 0) return
        _petSelectEvent.emit(
            PetSelectEvent.SelectPets(
                pets = selectedPets.map { it.id },
            ),
        )
    }

    override fun cancelSelection() {
        _petSelectEvent.emit(PetSelectEvent.CancelSelection)
    }

    companion object {
        fun factory(getPetsMineUseCase: GetPetsMineUseCase): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                PetSelectViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                )
            }
        }
    }
}
