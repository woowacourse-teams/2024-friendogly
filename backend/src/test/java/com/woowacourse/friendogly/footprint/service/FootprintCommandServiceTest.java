package com.woowacourse.friendogly.footprint.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FootprintCommandServiceTest {

    @Autowired
    private FootprintCommandService footprintCommandService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("발자국 저장")
    @Test
    void save() {
        memberRepository.save(
            Member.builder()
                .name("name")
                .email("test@test.com")
                .build()
        );

        Long savedId = footprintCommandService.save(new SaveFootprintRequest(1L, 30.0, 30.0));
        assertThat(savedId).isEqualTo(1L);
    }
}
