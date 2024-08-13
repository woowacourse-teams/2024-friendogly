package com.happy.friendogly.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.dto.request.UpdateMemberRequest;
import com.happy.friendogly.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

public class MemberCommandServiceTest extends ServiceTest {

    @Autowired
    private MemberCommandService memberCommandService;

    @DisplayName("회원 이름을 변경하는 경우, 변경된 회원 이름이 조회된다.")
    @Test
    void update_MemberName() {
        Member member = memberRepository.save(new Member("member", "tag", "imageUrl"));
        String newName = "newName";
        UpdateMemberRequest request = new UpdateMemberRequest(newName, "imageUrl", "imageUrl");

        memberCommandService.update(member.getId(), request, new MockMultipartFile("image", "image".getBytes()));
        Member findMember = memberRepository.getById(member.getId());

        assertThat(findMember.getName().getValue()).isEqualTo(newName);
    }
}
