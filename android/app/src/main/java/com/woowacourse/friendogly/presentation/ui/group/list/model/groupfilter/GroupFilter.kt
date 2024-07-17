package com.woowacourse.friendogly.presentation.ui.group.list.model.groupfilter

sealed class GroupFilter(val filterName: String) {
    class GenderFilter(filterName: String) : GroupFilter(filterName) {
        companion object {
            private fun from(): List<GroupFilter> {
                return listOf(
                    GenderFilter("수컷"),
                    GenderFilter("암컷"),
                    GenderFilter("중성화 수컷"),
                    GenderFilter("중성화 암컷"),
                )
            }
        }
    }

    class SizeFilter(filterName: String) : GroupFilter(filterName) {
        companion object {
            private fun from(): List<GroupFilter> {
                return listOf(
                    SizeFilter("대형견"),
                    SizeFilter("중형견"),
                    SizeFilter("소형견"),
                )
            }
        }
    }
}
