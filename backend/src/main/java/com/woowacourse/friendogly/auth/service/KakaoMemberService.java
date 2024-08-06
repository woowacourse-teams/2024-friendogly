package com.woowacourse.friendogly.auth.service;

import com.woowacourse.friendogly.auth.dto.KakaoLoginResponse;
import com.woowacourse.friendogly.auth.dto.TokenResponse;
import com.woowacourse.friendogly.auth.repository.KakaoMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KakaoMemberService {

    private final KakaoMemberRepository kakaoMemberRepository;

    public KakaoMemberService(KakaoMemberRepository kakaoMemberRepository) {
        this.kakaoMemberRepository = kakaoMemberRepository;
    }

    public KakaoLoginResponse login(Long kakaoMemberId) {
        if (isNewMember(kakaoMemberId)) {
            return KakaoLoginResponse.ofNotRegistered();
        }
        //TODO : jwt 만들면 여기 해야댐
        return KakaoLoginResponse.ofRegistered(new TokenResponse("asdf", "ASdfadsf"));
    }

    private boolean isNewMember(Long kakaoMemberId) {
        return !kakaoMemberRepository.existsByKakaoMemberId(kakaoMemberId);
    }
}
