package com.happy.friendogly.presentation.utils

import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.firebase.analytics.ParamKeys
import com.happy.friendogly.firebase.analytics.Types

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

fun AnalyticsHelper.logPlaygroundFragmentSwitched() {
    logEvent(type = Types.PLAYGROUND_FRAGMENT_SWITCHED)
}

fun AnalyticsHelper.logChatListFragmentSwitched() {
    logEvent(type = Types.CHAT_LIST_FRAGMENT_SWITCHED)
}

fun AnalyticsHelper.logMyPageFragmentSwitched() {
    logEvent(type = Types.MY_PAGE_FRAGMENT_SWITCHED)
}

fun AnalyticsHelper.logPetExistenceBtnClicked() {
    logEvent(type = Types.PET_EXISTENCE_BTN_CLICKED)
}

fun AnalyticsHelper.logRegisterMarkerBtnClicked() {
    logEvent(type = Types.REGISTER_MARKER_BTN_CLICKED)
}

fun AnalyticsHelper.logLocationBtnClicked() {
    logEvent(type = Types.LOCATION_BTN_CLICKED)
}

fun AnalyticsHelper.logMyPlaygroundBtnClicked() {
    logEvent(type = Types.MY_PLAYGROUND_BTN_CLICKED)
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

fun AnalyticsHelper.logPlaygroundMemberNameClicked() {
    logEvent(type = Types.PLAYGROUND_MEMBER_NAME_CLICKED)
}

fun AnalyticsHelper.logHelpBtnClicked() {
    logEvent(type = Types.HELP_BTN_CLICKED)
}

fun AnalyticsHelper.logPetImageClicked() {
    logEvent(type = Types.PET_IMAGE_CLICKED)
}

fun AnalyticsHelper.logPlaygroundSize(size: Int) {
    logEvent(type = Types.WOOF_FRAGMENT, ParamKeys.PLAYGROUND_SIZE to size)
}

fun AnalyticsHelper.logPetExistence(isExistPet: Boolean) {
    logEvent(
        type = Types.WOOF_FRAGMENT,
        ParamKeys.PET_EXISTENCE to isExistPet,
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

fun AnalyticsHelper.logChatAlarmClicked() {
    logEvent(
        type = Types.CHAT_ALARM_CLICKED,
    )
}

fun AnalyticsHelper.logChatSendMessageClicked() {
    logEvent(
        type = Types.CHAT_SEND_MESSAGE_CLICKED,
    )
}

fun AnalyticsHelper.logPermissionAlarmDenied() {
    logEvent(
        type = Types.CHAT_PERMISSION_ALARM_DENIED,
    )
}

fun AnalyticsHelper.logPermissionLocationDenied() {
    logEvent(
        type = Types.CHAT_PERMISSION_LOCATION_DENIED,
    )
}

fun AnalyticsHelper.logGoClubFromChatClicked() {
    logEvent(
        type = Types.CHAT_GO_CLUB_DETAIL_CLICKED,
    )
}
