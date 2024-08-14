package com.happy.friendogly.kakaoMember.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.happy.friendogly.auth.domain.KakaoMember;
import com.happy.friendogly.auth.repository.KakaoMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class KakaoMemberRepositoryTest {

    @Autowired
    private KakaoMemberRepository kakaoMemberRepository;

    @BeforeEach
    void cleanUp() {
        kakaoMemberRepository.deleteAll();
    }

    @DisplayName("중복된 카카오 회원 id를 가진 kakaoMember 저장 시 예외가 발생한다.")
    @Test
    void save_Fail_DuplicateKakaoMemberId() {
        String duplicateKakaoMemberId = "duplicated";
        KakaoMember kakaoMember1 = new KakaoMember(duplicateKakaoMemberId, 1L, "refreshToken");
        KakaoMember kakaoMember2 = new KakaoMember(duplicateKakaoMemberId, 2L, "refreshToken");

        kakaoMemberRepository.save(kakaoMember1);

        assertThatThrownBy(() -> kakaoMemberRepository.save(kakaoMember2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
