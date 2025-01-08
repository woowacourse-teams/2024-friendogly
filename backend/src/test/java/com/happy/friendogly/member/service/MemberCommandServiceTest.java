package com.happy.friendogly.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.dto.request.SaveClubMemberRequest;
import com.happy.friendogly.club.service.ClubCommandService;
import com.happy.friendogly.infra.ImageUpdateType;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.dto.request.UpdateMemberRequest;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.support.ServiceTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

public class MemberCommandServiceTest extends ServiceTest {

    @Autowired
    private MemberCommandService memberCommandService;

    @Autowired
    private ClubCommandService clubCommandService;

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

    @DisplayName("모임에 참여한 회원이 탈퇴하는 경우, 모임에서도 탈퇴된다.")
    @Transactional
    @Test
    void delete_DeleteFromClub() {
        // given
        Member member1 = memberRepository.save(new Member("member1", "tag", "imageUrl"));
        Member member2 = memberRepository.save(new Member("member2", "tag", "imageUrl"));
        Pet pet1 = petRepository.save(new Pet(
                member1,
                "강아지1",
                "강아지 설명",
                LocalDate.now().minusDays(1),
                SizeType.SMALL,
                Gender.FEMALE_NEUTERED,
                ""
        ));
        Pet pet2 = petRepository.save(new Pet(
                member2,
                "강아지2",
                "강아지 설명",
                LocalDate.now().minusDays(1),
                SizeType.MEDIUM,
                Gender.MALE,
                ""
        ));
        Club club = clubRepository.save(Club.create(
                "모임",
                "모임 설명",
                "서울특별시",
                "송파구",
                "신천동",
                5,
                member1,
                Set.of(Gender.MALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL, SizeType.MEDIUM),
                "",
                List.of(pet1)
        ));

        // when
        clubCommandService.joinClub(club.getId(), member2.getId(), new SaveClubMemberRequest(List.of(pet2.getId())));
        memberCommandService.delete(member2.getId());

        // then
        assertThat(club.countClubMember()).isEqualTo(1);
    }
}
