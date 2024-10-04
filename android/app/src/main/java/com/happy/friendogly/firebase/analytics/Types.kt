package com.happy.friendogly.firebase.analytics

/**
 * 로깅 전략 및 이벤트 로깅 시나리오
 * - 카카오 로그인 버튼을 눌렀을 때
 * - 구글 로그인 버튼을 눌렀을 때
 * - 프로필 등록이 되었을 때
 * - 로그인이 되었을 때
 * - 회원가입이 되었을 때
 * - 알림 권한 요청 다이얼로그가 표시되었을 때
 * - 알림 권한 요청 다이얼로그가 닫혔을 때
 * - 알림 권한 허용 버튼을 클릭했을 때
 * - 알림 권한이 허용되었을 때
 * - 알림 권한이 거부되었을 때
 * - OO 버튼을 클릭했을 때
 * - OO 알림이 표시되었을 때
 * - 홈 탭을 눌렀을 때
 * - 멍멍짖기 탭을 눌렀을 때
 * - 채팅 탭을 눌렀을 때
 * - 마이페이지 탭을 눌렀을 때
 * - 펫이 등록되었을 때
 * - 펫 편집이 되었을 때
 * - OO화면에서 뒤로가기 버튼을 클릭했을 때
 *
 * 위에는 예시입니다.
 * 본인이 필요한 곳이나 우리 서비스에서 가져가면 좋을 것 같은 수치가 있다면 이벤트를 발생시켜주세요
 * */

object Types {
    const val KAKAO_LOGIN_CLICKED = "kakao_login_clicked"
    const val GOOGLE_LOGIN_CLICKED = "google_login_clicked"
    const val GROUP_LIST_FRAGMENT = "group_list_fragment"
    const val WOOF_FRAGMENT = "woof_fragment"
    const val CHAT_LIST_FRAGMENT = "chat_list_fragment"
    const val MY_PAGE_FRAGMENT = "my_page_fragment"
    const val CLUB_LIST_FRAGMENT_SWITCHED = "group_list_fragment_switched"
    const val WOOF_FRAGMENT_SWITCHED = "woof_fragment_switched"
    const val CHAT_LIST_FRAGMENT_SWITCHED = "chat_list_fragment_switched"
    const val MY_PAGE_FRAGMENT_SWITCHED = "my_page_fragment_switched"
    const val PET_EXISTENCE_BTN_CLICKED = "pet_existence_btn_clicked"
    const val REGISTER_MARKER_BTN_CLICKED = "register_marker_btn_clicked"
    const val LOCATION_BTN_CLICKED = "location_btn_clicked"
    const val MY_FOOTPRINT_BTN_CLICKED = "my_footprint_btn_clicked"
    const val REFRESH_BTN_CLICKED = "refresh_btn_clicked"
    const val BACK_BTN_CLICKED = "back_btn_clicked"
    const val CLOSE_BTN_CLICKED = "close_btn_clicked"
    const val FOOTPRINT_MEMBER_NAME_CLICKED = "footprint_member_name_clicked"
    const val HELP_BTN_CLICKED = "help_btn_clicked"
    const val PET_IMAGE_CLICKED = "pet_image_clicked"
    const val CLUB_SELECT_PARTICIPATION_FILTER = "club_select_participation_filter"
    const val CLUB_SELECT_CLUB_FILTER = "club_select_club_filter"
    const val CLUB_ADD_UN_SELECT_FILTER = "club_add_un_select_filter"
    const val CLUB_ADD_MEMBER_COUNT = "club_select_member_count"
    const val CLUB_ADD_CLICKED = "club_add_clicked"
    const val CLUB_DELETE_CLICKED = "club_delete_clicked"
    const val CLUB_PARTICIPATE_CLICKED = "club_participate_clicked"
    const val CLUB_UPDATE_CLICKED = "club_update_clicked"
    const val CLUB_UPDATE_LOCATION_CLICKED = "club_update_location_clicked"
    const val CLUB_CLUB_DETAIL_CLICKED = "club_detail_clicked"
    const val CLUB_MY_CLICKED = "club_detail_clicked"
    const val CLUB_MY_ADD_BTN_CLICKED = "club_my_add_btn_clicked"
    const val CLUB_LIST_ADD_BTN_CLICKED = "club_list_add_btn_clicked"
    const val CHAT_ALARM_CLICKED = "chat_alarm_clicked"
    const val CHAT_SEND_MESSAGE_CLICKED = "chat_send_message_clicked"
    const val CHAT_PERMISSION_ALARM_DENIED = "chat_permission_alarm_denied"
    const val CHAT_PERMISSION_LOCATION_DENIED = "chat_permission_location_denied"
    const val CHAT_GO_CLUB_DETAIL_CLICKED = "chat_go_club_detail_clicked"
}
