package com.woowacourse.friendogly.presentation.ui.group.add.model

class GroupCounter(val count: Int = MAX_COUNT) {
    companion object {
        private const val MAX_COUNT = 5
    }
}
