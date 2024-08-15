package com.happy.friendogly.member.service;

import com.happy.friendogly.infra.ImageUpdateType;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.dto.request.UpdateMemberRequest;
import com.happy.friendogly.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberCommandServiceTest extends ServiceTest {

    @Autowired
    private MemberCommandService memberCommandService;

    @DisplayName("회원 이름을 변경하는 경우, 변경된 회원 이름이 조회된다.")
    @Test
    void update_MemberName() {
        Member member = memberRepository.save(new Member("member", "tag", "imageUrl"));
        String newName = "newName";
        UpdateMemberRequest request = new UpdateMemberRequest(newName, ImageUpdateType.NOT_UPDATE.name());

        memberCommandService.update(member.getId(), request, null);
        Member findMember = memberRepository.getById(member.getId());

        assertThat(findMember.getName().getValue()).isEqualTo(newName);
    }

    @DisplayName("회원 프로필 이미지를 변경하는 경우, 변경된 이미지 url이 조회된다.")
    @Test
    void update_MemberImage() {
        Member member = memberRepository.save(new Member("member", "tag", "imageUrl"));
        UpdateMemberRequest request = new UpdateMemberRequest("member", ImageUpdateType.UPDATE.name());
        String newImageFileName = "newImageFileName.jpg";
        MockMultipartFile newImage = new MockMultipartFile("image", newImageFileName, null, new byte[1]);

        memberCommandService.update(member.getId(), request, newImage);
        Member findMember = memberRepository.getById(member.getId());

        assertThat(findMember.getImageUrl()).contains(newImageFileName);
    }

    @DisplayName("회원 프로필 이미지를 변경하지 않는 경우, 기존의 이미지 url이 조회된다.")
    @Test
    void notUpdate_MemberImage() {
        String imageUrl = "imageUrl";
        Member member = memberRepository.save(new Member("member", "tag", imageUrl));
        UpdateMemberRequest request = new UpdateMemberRequest("member", ImageUpdateType.NOT_UPDATE.name());

        memberCommandService.update(member.getId(), request, null);
        Member findMember = memberRepository.getById(member.getId());

        assertThat(findMember.getImageUrl()).isEqualTo(imageUrl);
    }

    @DisplayName("회원 프로필 이미지를 기본 이미지로 변경하는 경우, 빈 문자열이 조회된다.")
    @Test
    void update_DeleteMemberImage() {
        Member member = memberRepository.save(new Member("member", "tag", "imageUrl"));
        UpdateMemberRequest request = new UpdateMemberRequest("member", ImageUpdateType.DELETE.name());

        memberCommandService.update(member.getId(), request, null);
        Member findMember = memberRepository.getById(member.getId());

        assertThat(findMember.getImageUrl()).isEmpty();
    }
}
