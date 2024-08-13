package com.happy.friendogly.analytics

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
    const val MARK_BTN_CLICKED = "mark_btn_clicked"
    const val REGISTER_MARKER_BTN_CLICKED = "register_marker_btn_clicked"
    const val LOCATION_BTN_CLICKED = "location_btn_clicked"
    const val MY_FOOTPRINT_BTN_CLICKED = "my_footprint_btn_clicked"
    const val BACK_BTN_CLICKED = "back_btn_clicked"
    const val CLOSE_BTN_CLICKED = "close_btn_clicked"
    const val FOOTPRINT_CLICKED = "footprint_clicked"
    const val FOOTPRINT_MEMBER_NAME_CLICKED = "footprint_member_name_clicked"
}
