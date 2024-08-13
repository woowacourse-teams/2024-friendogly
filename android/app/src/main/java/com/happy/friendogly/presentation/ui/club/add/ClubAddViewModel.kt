package com.happy.friendogly.presentation.ui.club.add

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.model.UserAddress
import com.happy.friendogly.domain.usecase.GetAddressUseCase
import com.happy.friendogly.domain.usecase.PostClubUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.club.add.adapter.ClubAddAdapter.Companion.MAX_PAGE_SIZE
import com.happy.friendogly.presentation.ui.club.add.adapter.ClubAddAdapter.Companion.MIN_PAGE
import com.happy.friendogly.presentation.ui.club.add.model.ClubCounter
import com.happy.friendogly.presentation.ui.club.common.model.ClubFilterSelector
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.filter.ClubFilterItemActionHandler
import com.happy.friendogly.presentation.ui.club.common.mapper.toDomain
import com.happy.friendogly.presentation.ui.club.common.mapper.toGenders
import com.happy.friendogly.presentation.ui.club.common.mapper.toSizeTypes
import com.happy.friendogly.presentation.utils.addSourceList
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ClubAddViewModel(
    private val getAddressUseCase: GetAddressUseCase,
    private val postClubUseCase: PostClubUseCase,
) : BaseViewModel(), ClubAddActionHandler, ClubFilterItemActionHandler {
    private val _myAddress: MutableLiveData<UserAddress> = MutableLiveData()
    val myAddress: LiveData<UserAddress> get() = _myAddress

    private val _clubAddEvent: MutableLiveData<Event<ClubAddEvent>> =
        MutableLiveData()
    val clubAddEvent: LiveData<Event<ClubAddEvent>> get() = _clubAddEvent

    private val _currentPage: MutableLiveData<Int> = MutableLiveData(MIN_PAGE)
    val currentPage: LiveData<Int> get() = _currentPage

    private val clubFilterSelector = ClubFilterSelector()

    private val _clubCounter: MutableLiveData<ClubCounter> = MutableLiveData(ClubCounter())
    val clubCounter: LiveData<ClubCounter> get() = _clubCounter

    val clubTitle: MutableLiveData<String> = MutableLiveData("")

    val clubContent: MutableLiveData<String> = MutableLiveData("")

    private val _clubPoster: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val clubPoster: LiveData<Bitmap?> get() = _clubPoster

    val validNextPage: MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>()
            .apply {
                addSourceList(
                    clubTitle,
                    clubContent,
                    clubFilterSelector.currentSelectedFilters,
                    clubCounter,
                    myAddress,
                ) {
                    isValidAddedData()
                }
            }

    val validPrevPage:
        MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>()
            .apply {
                addSourceList(currentPage) {
                    isValidPrevPage()
                }
            }

    init {
        loadAddress()
        initAllFilter()
    }

    fun settingClubCounter(count: Int) {
        _clubCounter.value = ClubCounter(count)
    }

    fun updateClubPoster(bitmap: Bitmap? = null) {
        _clubPoster.value = bitmap
    }

    private fun initAllFilter() {
        clubFilterSelector.initClubFilter(ClubFilter.makeClubFilterEntry())
    }

    private fun loadAddress() =
        viewModelScope.launch {
            getAddressUseCase()
                .onSuccess {
                    _myAddress.value = it
                }
                .onFailure {
                    _clubAddEvent.emit(ClubAddEvent.FailLoadAddress)
                }
        }

    private fun isValidAddedData(): Boolean {
        return isValidEditGroup() && isValidFilterGroup() && isValidClubCount() && isValidAddress()
    }

    private fun isValidPage(page: Int): Boolean {
        return page in MIN_PAGE until MAX_PAGE_SIZE
    }

    private fun isSubmitPage(page: Int): Boolean {
        return page == MAX_PAGE_SIZE
    }

    private fun isValidPrevPage(): Boolean {
        val page = currentPage.value ?: MIN_PAGE
        return page in MIN_PAGE + 1 until MAX_PAGE_SIZE
    }

    private fun isValidEditGroup(): Boolean {
        val clubTitleLength = clubTitle.value?.length ?: (MIN_TEXT_LENGTH - 1)
        val clubContentLength = clubContent.value?.length ?: (MIN_TEXT_LENGTH - 1)
        return clubTitleLength in MIN_TEXT_LENGTH..MAX_TITLE_LENGTH &&
            clubContentLength in MIN_TEXT_LENGTH..MAX_CONTENT_LENGTH
    }

    private fun isValidFilterGroup(): Boolean {
        return with(clubFilterSelector) {
            isContainSizeFilter() && isContainGenderFilter()
        }
    }

    private fun isValidClubCount(): Boolean {
        return clubCounter.value?.isValid() ?: false
    }

    private fun isValidAddress(): Boolean {
        return myAddress.value != null
    }

    override fun selectClubFilter(
        filterName: String,
        isSelected: Boolean,
    ) {
        val clubFilter = ClubFilter.findClubFilter(filterName) ?: return
        if (isSelected) {
            clubFilterSelector.addClubFilter(clubFilter)
        } else {
            clubFilterSelector.removeClubFilter(clubFilter)
        }
    }

    override fun cancelAddClub() {
        _clubAddEvent.emit(ClubAddEvent.Navigation.NavigateToHome)
    }

    fun submitAddClub(
        dogs: List<Long>,
        file: MultipartBody.Part?,
    ) = viewModelScope.launch {
        postClubUseCase(
            title = clubTitle.value ?: return@launch,
            content = clubContent.value ?: return@launch,
            address = myAddress.value?.toDomain() ?: return@launch,
            allowedGender = clubFilterSelector.selectGenderFilters().toGenders(),
            allowedSize = clubFilterSelector.selectSizeFilters().toSizeTypes(),
            memberCapacity = clubCounter.value?.count ?: return@launch,
            file = file,
            petIds = dogs,
        )
            .onSuccess {
                _clubAddEvent.emit(ClubAddEvent.Navigation.NavigateToHomeWithAdded)
            }
            .onFailure {
                _clubAddEvent.emit(ClubAddEvent.FailAddClub)
            }
    }

    override fun navigatePrevPage() {
        val currentPage = _currentPage.value ?: return
        val newPage = currentPage - 1
        if (newPage in MIN_PAGE until MAX_PAGE_SIZE) {
            _currentPage.value = newPage
            _clubAddEvent.emit(ClubAddEvent.ChangePage(newPage))
        }
    }

    override fun navigateNextPage() {
        val currentPage = _currentPage.value ?: return
        val newPage = currentPage + 1
        if (isValidPage(newPage)) {
            _currentPage.value = newPage
            _clubAddEvent.emit(ClubAddEvent.ChangePage(newPage))
        } else if (isSubmitPage(newPage)) {
            selectDogs()
        }
    }

    private fun selectDogs() {
        val filters = clubFilterSelector.currentSelectedFilters.value ?: emptyList()
        _clubAddEvent.emit(ClubAddEvent.Navigation.NavigateToSelectDog(filters))
    }

    override fun selectClubImage() {
        _clubAddEvent.emit(ClubAddEvent.Navigation.NavigateToSelectClubPoster)
    }

    companion object {
        private const val MIN_TEXT_LENGTH = 1
        private const val MAX_TITLE_LENGTH = 100
        private const val MAX_CONTENT_LENGTH = 1000

        fun factory(
            getAddressUseCase: GetAddressUseCase,
            postClubUseCase: PostClubUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                ClubAddViewModel(
                    getAddressUseCase = getAddressUseCase,
                    postClubUseCase = postClubUseCase,
                )
            }
        }
    }
}
