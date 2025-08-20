package com.happy.friendogly.pet.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PetRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PetRepository petRepository;

    @DisplayName("다수의 멤버 ID로 멤버들의 펫을 조회할 수 있다.")
    @Disabled
    @Test
    void findAllByMemberIdIn() {
        // given
        Member savedMember1 = memberRepository.save(new Member("member1", "tag1", "imageUrl1"));
        Member savedMember2 = memberRepository.save(new Member("member2", "tag2", "imageUrl2"));
        petRepository.save(
                new Pet(
                        savedMember1,
                        "name1",
                        "description1",
                        LocalDate.now(),
                        SizeType.LARGE,
                        Gender.FEMALE,
                        "imageUrl1"
                )
        );
        petRepository.save(
                new Pet(
                        savedMember2,
                        "name2",
                        "description2",
                        LocalDate.now(),
                        SizeType.LARGE,
                        Gender.FEMALE,
                        "imageUrl2"
                )
        );

        // when
        List<Pet> pets = petRepository.findAllByMemberIds(
                List.of(savedMember1.getId(), savedMember2.getId()));

        // then
        assertThat(pets).hasSize(2);
    }
}