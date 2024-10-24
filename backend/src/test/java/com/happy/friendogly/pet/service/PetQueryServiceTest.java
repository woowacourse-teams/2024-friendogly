package com.happy.friendogly.pet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.pet.dto.response.FindPetExistenceResponse;
import com.happy.friendogly.pet.dto.response.FindPetResponse;
import com.happy.friendogly.pet.repository.PetRepository;
import com.happy.friendogly.support.ServiceTest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PetQueryServiceTest extends ServiceTest {

    @Autowired
    private PetQueryService petQueryService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PetRepository petRepository;

    @DisplayName("pet 단건 조회 테스트")
    @Test
    void findById() {
        Member member = memberRepository.save(new Member("member", "a1b2c3d4", "http://www.friendogly.com"));
        Pet pet = petRepository.save(
                new Pet(
                        member,
                        "땡이",
                        "땡이에용",
                        LocalDate.now().minusDays(1L),
                        SizeType.SMALL,
                        Gender.FEMALE_NEUTERED,
                        "http://www.friendogly.com"
                )
        );

        FindPetResponse response = petQueryService.findById(pet.getId());

        assertAll(
                () -> assertThat(response.id()).isEqualTo(pet.getId()),
                () -> assertThat(response.memberId()).isEqualTo(pet.getMember().getId())
        );
    }

    @DisplayName("memberId로 pet 단건 조회 테스트")
    @Test
    void findByMemberId() {
        Member member = memberRepository.save(new Member("member", "a1b2c3d4", "http://www.friendogly.com"));
        Pet pet = petRepository.save(
                new Pet(
                        member,
                        "땡이",
                        "땡이에용",
                        LocalDate.now().minusDays(1L),
                        SizeType.SMALL,
                        Gender.FEMALE_NEUTERED,
                        "http://www.friendogly.com"
                )
        );

        List<FindPetResponse> responses = petQueryService.findByMemberId(member.getId());

        assertAll(
                () -> assertThat(responses.size()).isEqualTo(1),
                () -> assertThat(responses.get(0).id()).isEqualTo(pet.getId())
        );
    }

    @DisplayName("멤버가 가진 펫이 한마리라도 있는 지 알 수 있다 : False")
    @Test
    void existMineFalse() {
        // given
        Member member = memberRepository.save(
                new Member("김도선", "tag1", "imageUrl")
        );

        // when
        FindPetExistenceResponse response = petQueryService.checkPetExistence(member.getId());

        // then
        assertThat(response.isExistPet()).isFalse();
    }

    @DisplayName("멤버가 가진 펫이 한마리라도 있는 지 알 수 있다 : True")
    @Test
    void existMineTrue() {
        // given
        Member member = memberRepository.save(
                new Member("김도선", "tag1", "imageUrl")
        );
        petRepository.save(
                new Pet(
                        member,
                        "petName",
                        "description",
                        LocalDate.of(2023, 10, 4),
                        SizeType.LARGE,
                        Gender.FEMALE,
                        "imgaeUrl"
                )
        );

        // when
        FindPetExistenceResponse response = petQueryService.checkPetExistence(member.getId());

        // then
        assertThat(response.isExistPet()).isTrue();
    }
}
