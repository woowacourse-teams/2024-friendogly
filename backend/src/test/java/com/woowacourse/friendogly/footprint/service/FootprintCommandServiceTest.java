package com.woowacourse.friendogly.footprint.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.repository.FootprintRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
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

    @Autowired
    private FootprintRepository footprintRepository;

    @AfterEach
    void cleanUp() {
        footprintRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("발자국 저장")
    @Test
    void save() {
        Member member = memberRepository.save(
            Member.builder()
                .name("name")
                .email("test@test.com")
                .build()
        );

        footprintCommandService.save(new SaveFootprintRequest(member.getId(), 30.0, 30.0));
        assertThat(footprintRepository.findAll()).hasSize(1);
    }
}
