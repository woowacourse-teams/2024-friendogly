package com.happy.friendogly.presentation.utils

import com.happy.friendogly.analytics.AnalyticsHelper
import com.happy.friendogly.analytics.ParamKeys
import com.happy.friendogly.analytics.Types

fun AnalyticsHelper.logKakaoLoginClicked() {
    logEvent(
        type = Types.KAKAO_LOGIN_CLICKED,
        ParamKeys.LOGIN_ATTEMPT_TIME to System.currentTimeMillis(),
    )
}

fun AnalyticsHelper.logGoogleLoginClicked() {
    logEvent(
        type = Types.GOOGLE_LOGIN_CLICKED,
        ParamKeys.LOGIN_ATTEMPT_TIME to System.currentTimeMillis(),
    )
}

fun AnalyticsHelper.logClubListFragmentSwitched() {
    logEvent(type = Types.CLUB_LIST_FRAGMENT_SWITCHED)
}

fun AnalyticsHelper.logWoofFragmentSwitched() {
    logEvent(type = Types.WOOF_FRAGMENT_SWITCHED)
}

fun AnalyticsHelper.logChatListFragmentSwitched() {
    logEvent(type = Types.CHAT_LIST_FRAGMENT_SWITCHED)
}

fun AnalyticsHelper.logMyPageFragmentSwitched() {
    logEvent(type = Types.MY_PAGE_FRAGMENT_SWITCHED)
}

fun AnalyticsHelper.logMarkBtnClicked() {
    logEvent(type = Types.MARK_BTN_CLICKED)
}

fun AnalyticsHelper.logRegisterMarkerBtnClicked() {
    logEvent(type = Types.REGISTER_MARKER_BTN_CLICKED)
}

fun AnalyticsHelper.logLocationBtnClicked() {
    logEvent(type = Types.LOCATION_BTN_CLICKED)
}

fun AnalyticsHelper.logMyFootprintBtnClicked() {
    logEvent(type = Types.MY_FOOTPRINT_BTN_CLICKED)
}

fun AnalyticsHelper.logRefreshBtnClicked() {
    logEvent(type = Types.REFRESH_BTN_CLICKED)
}

fun AnalyticsHelper.logBackBtnClicked() {
    logEvent(type = Types.BACK_BTN_CLICKED)
}

fun AnalyticsHelper.logCloseBtnClicked() {
    logEvent(type = Types.CLOSE_BTN_CLICKED)
}

fun AnalyticsHelper.logFootprintClicked() {
    logEvent(type = Types.FOOTPRINT_CLICKED)
}

fun AnalyticsHelper.logFootprintMemberNameClicked() {
    logEvent(type = Types.FOOTPRINT_MEMBER_NAME_CLICKED)
}

fun AnalyticsHelper.logRegisterHelpClicked() {
    logEvent(type = Types.REGISTER_HELP_CLICKED)
}

fun AnalyticsHelper.logWalkHelpClicked() {
    logEvent(type = Types.WALK_HELP_CLICKED)
}

fun AnalyticsHelper.logFootprintPetImageClicked() {
    logEvent(type = Types.FOOTPRINT_PET_IMAGE_CLICKED)
}

fun AnalyticsHelper.logNearFootprintSize(size: Int) {
    logEvent(type = Types.WOOF_FRAGMENT, ParamKeys.NEAR_FOOTPRINTS_SIZE to size)
}

fun AnalyticsHelper.logFootprintMarkBtnInfo(
    hasPet: Boolean,
    remainingTime: Int,
) {
    logEvent(
        type = Types.WOOF_FRAGMENT,
        ParamKeys.FOOTPRINT_MARK_BTN_HAS_PET to hasPet,
        ParamKeys.FOOTPRINT_MARK_BTN_REMAINING_TIME to remainingTime,
    )
}

fun AnalyticsHelper.logSelectParticipationFilter(filterName: String) {
    logEvent(
        type = Types.CLUB_SELECT_PARTICIPATION_FILTER,
        ParamKeys.CLUB_PARTICIPATION_FILTER to filterName,
    )
}

fun AnalyticsHelper.logSelectClubFilter(filterName: String) {
    logEvent(
        type = Types.CLUB_SELECT_CLUB_FILTER,
        ParamKeys.CLUB_CLUB_FILTER to filterName,
    )
}

fun AnalyticsHelper.logUnSelectAddClubFilter(filterName: String) {
    logEvent(
        type = Types.CLUB_ADD_UN_SELECT_FILTER,
        ParamKeys.CLUB_ADD_UN_SELECT_FILTER to filterName,
    )
}

fun AnalyticsHelper.logAddClubClick() {
    logEvent(
        type = Types.CLUB_ADD_CLICKED,
    )
}

fun AnalyticsHelper.logSelectClubMemberCount(count: Int) {
    logEvent(
        type = Types.CLUB_ADD_MEMBER_COUNT,
        ParamKeys.CLUB_MEMBER_COUNT to count,
    )
}

fun AnalyticsHelper.logDeleteMemberClick() {
    logEvent(
        type = Types.CLUB_DELETE_CLICKED,
    )
}

fun AnalyticsHelper.logParticipateClick() {
    logEvent(
        type = Types.CLUB_PARTICIPATE_CLICKED,
    )
}

fun AnalyticsHelper.logUpdateClubClick() {
    logEvent(
        type = Types.CLUB_UPDATE_CLICKED,
    )
}

fun AnalyticsHelper.logUpdateUserLocation() {
    logEvent(
        type = Types.CLUB_UPDATE_LOCATION_CLICKED,
    )
}

fun AnalyticsHelper.logClubDetailClick() {
    logEvent(
        type = Types.CLUB_CLUB_DETAIL_CLICKED,
    )
}

fun AnalyticsHelper.logMyClubClick() {
    logEvent(
        type = Types.CLUB_MY_CLICKED,
    )
}

fun AnalyticsHelper.logMyAddClubClick() {
    logEvent(
        type = Types.CLUB_MY_ADD_BTN_CLICKED,
    )
}

fun AnalyticsHelper.logListAddClubClick() {
    logEvent(
        type = Types.CLUB_LIST_ADD_BTN_CLICKED,
    )
}
