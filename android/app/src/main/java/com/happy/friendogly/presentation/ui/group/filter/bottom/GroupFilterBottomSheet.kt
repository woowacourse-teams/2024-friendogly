package com.happy.friendogly.presentation.ui.group.filter.bottom

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.happy.friendogly.presentation.ui.group.model.groupfilter.ParticipationFilter

class GroupFilterBottomSheet(
    currentFilters: List<GroupFilter>,
    selectFilters: (filters: List<GroupFilter>) -> Unit,
    close: () -> Unit,
) : BottomSheetDialogFragment() {
}
