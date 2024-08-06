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
}
