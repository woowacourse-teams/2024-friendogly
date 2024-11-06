package com.happy.friendogly.presentation.ui.club.common.model

import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.utils.InstantTaskExecutorExtension
import com.happy.friendogly.utils.getOrAwaitValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class ClubFilterSelectorTest {

    private lateinit var clubFilterSelector: ClubFilterSelector

    @BeforeEach
    fun setUp() {
        clubFilterSelector = ClubFilterSelector()
    }

    @Test
    fun `필터를 추가하면 ClubFilterSelector에 해당 필터가 추가되어야 한다`() {
        clubFilterSelector = ClubFilterSelector()
        // given
        val filter = ClubFilter.GenderFilter.Male

        // when
        clubFilterSelector.addClubFilter(filter)
        val actualFilter = clubFilterSelector.currentSelectedFilters.getOrAwaitValue()

        //then
        assertThat(actualFilter.firstOrNull()).isEqualTo(filter)
    }

    @Test
    fun `이미 추가 된 필터가 있다면 해당 필터는 추가되지 않아야 한다`() {
        clubFilterSelector = ClubFilterSelector()
        // given
        val filter = ClubFilter.GenderFilter.Male
        clubFilterSelector.addClubFilter(filter)

        // when
        clubFilterSelector.addClubFilter(filter)
        val actualFilter = clubFilterSelector.currentSelectedFilters.getOrAwaitValue()

        //then
        assertThat(
            actualFilter.count { it.filterName == filter.filterName }
        ).isEqualTo(1)
    }

    @Test
    fun `여러 필터를 추가하면 ClubFilterSelector에 모든 필터가 추가되어야 한다`() {
        // given
        val filter1 = ClubFilter.GenderFilter.Male
        val filter2 = ClubFilter.SizeFilter.SmallDog

        // when
        clubFilterSelector.addClubFilter(filter1)
        clubFilterSelector.addClubFilter(filter2)
        val actualFilters = clubFilterSelector.currentSelectedFilters.getOrAwaitValue()

        // then
        assertThat(actualFilters).containsExactlyInAnyOrder(filter1, filter2)
    }

    @Test
    fun `필터를 제거하면 해당 필터는 ClubFilterSelector에서 삭제되어야 한다`() {
        // given
        val filter = ClubFilter.GenderFilter.Male
        clubFilterSelector.addClubFilter(filter)

        // when
        clubFilterSelector.removeClubFilter(filter)
        val actualFilters = clubFilterSelector.currentSelectedFilters.getOrAwaitValue()

        // then
        assertThat(actualFilters).doesNotContain(filter)
    }

    @Test
    fun `필터 목록을 초기화하면 주어진 필터들로 초기화되어야 한다`() {
        // given
        val filter1 = ClubFilter.GenderFilter.Male
        val filter2 = ClubFilter.SizeFilter.SmallDog
        val filtersToInit = listOf(filter1, filter2)

        // when
        clubFilterSelector.initClubFilter(filtersToInit)
        val actualFilters = clubFilterSelector.currentSelectedFilters.getOrAwaitValue()

        // then
        assertThat(actualFilters).containsExactlyInAnyOrder(filter1, filter2)
    }

    @Test
    fun `Gender 필터를 선택하면 해당 필터들만 반환되어야 한다`() {
        // given
        val filter1 = ClubFilter.GenderFilter.Male
        val filter2 = ClubFilter.SizeFilter.SmallDog
        clubFilterSelector.addClubFilter(filter1)
        clubFilterSelector.addClubFilter(filter2)

        // when
        val genderFilters = clubFilterSelector.selectGenderFilters()

        // then
        assertThat(genderFilters).containsExactly(ClubFilter.GenderFilter.Male)
    }

    @Test
    fun `Size 필터를 선택하면 해당 필터들만 반환되어야 한다`() {
        // given
        val filter1 = ClubFilter.GenderFilter.Male
        val filter2 = ClubFilter.SizeFilter.SmallDog
        clubFilterSelector.addClubFilter(filter1)
        clubFilterSelector.addClubFilter(filter2)

        // when
        val sizeFilters = clubFilterSelector.selectSizeFilters()

        // then
        assertThat(sizeFilters).containsExactly(ClubFilter.SizeFilter.SmallDog)
    }

    @Test
    fun `선택 된 GenderFilter가 있다면, GenderFilter를 확인하는 로직은 true를 반환해야 한다`() {
        // given
        clubFilterSelector.addClubFilter(ClubFilter.GenderFilter.Male)

        // when
        val containsGenderFilter = clubFilterSelector.isContainGenderFilter()

        // then
        assertThat(containsGenderFilter).isTrue()
    }

    @Test
    fun `선택 된 GenderFilter가 없다면, GenderFilter를 확인하는 로직은 false를 반환해야 한다`() {
        // given
        clubFilterSelector.addClubFilter(ClubFilter.SizeFilter.SmallDog)

        // when
        val containsGenderFilter = clubFilterSelector.isContainGenderFilter()

        // then
        assertThat(containsGenderFilter).isFalse()
    }

    @Test
    fun `선택 된 SizeFilter가 있다면, SizeFilter를 확인하는 로직은 true를 반환해야 한다`() {
        // given
        clubFilterSelector.addClubFilter(ClubFilter.SizeFilter.SmallDog)
        clubFilterSelector.addClubFilter(ClubFilter.SizeFilter.BigDog)

        // when
        val containsSizeFilter = clubFilterSelector.isContainSizeFilter()

        // then
        assertThat(containsSizeFilter).isTrue()
    }

    @Test
    fun `선택 된 SizeFilter가 없다면, SizeFilter를 확인하는 로직은 false를 반환해야 한다`() {
        // given
        clubFilterSelector.addClubFilter(ClubFilter.GenderFilter.Female)
        clubFilterSelector.addClubFilter(ClubFilter.GenderFilter.Male)

        // when
        val containsSizeFilter = clubFilterSelector.isContainSizeFilter()

        // then
        assertThat(containsSizeFilter).isFalse()
    }


}
