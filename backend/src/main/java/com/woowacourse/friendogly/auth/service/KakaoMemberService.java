package com.woowacourse.friendogly.auth.service;

import com.woowacourse.friendogly.auth.domain.KakaoMember;
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

    public void save(Long kakaoMemberId) {
        if (isNewMember(kakaoMemberId)) {
            kakaoMemberRepository.save(new KakaoMember(kakaoMemberId));
        }
    }

    private boolean isNewMember(Long kakaoMemberId) {
        return !kakaoMemberRepository.existsByKakaoMemberId(kakaoMemberId);
    }
}
