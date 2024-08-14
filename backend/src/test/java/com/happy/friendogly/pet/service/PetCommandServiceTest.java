package com.happy.friendogly.pet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.pet.dto.request.UpdatePetRequest;
import com.happy.friendogly.support.ServiceTest;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

class PetCommandServiceTest extends ServiceTest {

    @Autowired
    private PetCommandService petCommandService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("트레", "1b32cff0", "https://image.com/image.jpg"));
    }

    @DisplayName("펫 정보를 수정할 수 있다 (이미지 변경)")
    @Test
    void update() {
        // given
        Pet pet = petRepository.save(new Pet(
                member,
                "도토리",
                "귀여운 도토리입니다!",
                LocalDate.now().minusYears(1),
                SizeType.SMALL,
                Gender.MALE_NEUTERED,
                "https://picsum.photos/100"
        ));

        UpdatePetRequest request = new UpdatePetRequest(
                "바둑이",
                "새로운 펫이에요!",
                LocalDate.now().minusYears(2),
                "MEDIUM",
                "MALE",
                "UPDATE"
        );

        MockMultipartFile image = new MockMultipartFile("image", new byte[10]);

        // when
        petCommandService.update(member.getId(), pet.getId(), request, image);

        // then
        Pet newPet = petRepository.getById(pet.getId());
        assertAll(
                () -> assertThat(newPet.getId()).isEqualTo(pet.getId()),
                () -> assertThat(newPet.getMember().getId()).isEqualTo(pet.getMember().getId()),
                () -> assertThat(newPet.getName().getValue()).isEqualTo("바둑이"),
                () -> assertThat(newPet.getDescription().getValue()).isEqualTo("새로운 펫이에요!"),
                () -> assertThat(newPet.getBirthDate().getValue()).isEqualTo(LocalDate.now().minusYears(2)),
                () -> assertThat(newPet.getSizeType()).isEqualTo(SizeType.MEDIUM),
                () -> assertThat(newPet.getGender()).isEqualTo(Gender.MALE),
                () -> assertThat(newPet.getImageUrl()).startsWith("http://localhost/")
        );
    }

    @DisplayName("펫 정보를 수정할 수 있다 (이미지 변경 없음)")
    @Test
    void update_ImageNotUpdate() {
        // given
        Pet pet = petRepository.save(new Pet(
                member,
                "도토리",
                "귀여운 도토리입니다!",
                LocalDate.now().minusYears(1),
                SizeType.SMALL,
                Gender.MALE_NEUTERED,
                "https://picsum.photos/100"
        ));

        UpdatePetRequest request = new UpdatePetRequest(
                "바둑이",
                "새로운 펫이에요!",
                LocalDate.now().minusYears(2),
                "MEDIUM",
                "MALE",
                "NOT_UPDATE"
        );

        // when
        petCommandService.update(member.getId(), pet.getId(), request, null);

        // then
        Pet newPet = petRepository.getById(pet.getId());
        assertThat(newPet.getImageUrl()).isEqualTo("https://picsum.photos/100");
    }

    @DisplayName("펫 정보를 수정할 수 있다 (default 이미지로 변경)")
    @Test
    void update_ImageDelete() {
        // given
        Pet pet = petRepository.save(new Pet(
                member,
                "도토리",
                "귀여운 도토리입니다!",
                LocalDate.now().minusYears(1),
                SizeType.SMALL,
                Gender.MALE_NEUTERED,
                "https://picsum.photos/100"
        ));

        UpdatePetRequest request = new UpdatePetRequest(
                "바둑이",
                "새로운 펫이에요!",
                LocalDate.now().minusYears(2),
                "MEDIUM",
                "MALE",
                "DELETE"
        );

        // when
        petCommandService.update(member.getId(), pet.getId(), request, null);

        // then
        Pet newPet = petRepository.getById(pet.getId());
        assertThat(newPet.getImageUrl()).isBlank();
    }

    @DisplayName("자신의 펫이 아니면 펫 정보를 수정할 수 없다.")
    @Test
    void update_NotMyPet() {
        // given
        Member other = memberRepository.save(new Member("다른사람", "12cdd5fb", "https://image.com/image.jpg"));

        Pet pet = petRepository.save(new Pet(
                other,
                "도토리",
                "귀여운 도토리입니다!",
                LocalDate.now().minusYears(1),
                SizeType.SMALL,
                Gender.MALE_NEUTERED,
                "https://picsum.photos/100"
        ));

        UpdatePetRequest request = new UpdatePetRequest(
                "바둑이",
                "새로운 펫이에요!",
                LocalDate.now().minusYears(2),
                "MEDIUM",
                "MALE",
                "UPDATE"
        );

        // when - then
        assertThatThrownBy(() -> petCommandService.update(member.getId(), pet.getId(), request, null))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("자신의 강아지만 수정할 수 있습니다.");
    }
}
