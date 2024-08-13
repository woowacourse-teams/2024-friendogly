package com.happy.friendogly.pet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.pet.dto.request.UpdatePetRequest;
import com.happy.friendogly.support.ServiceTest;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

public class PetCommandServiceTest extends ServiceTest {

    @Autowired
    private PetCommandService petCommandService;

    @DisplayName("반려견 이름을 변경하는 경우, 변경된 반려견 이름이 조회된다.")
    @Test
    void update_PetName() {
        Member member = memberRepository.save(new Member("member", "tag", "imageUrl"));
        Pet pet = petRepository.save(
                new Pet(member, "name", "pet", LocalDate.now().minusDays(1L), SizeType.SMALL, Gender.MALE, "imageUrl"));

        String newName = "newName";
        UpdatePetRequest request = new UpdatePetRequest(
                newName,
                pet.getDescription().getValue(),
                pet.getBirthDate().getValue(),
                pet.getSizeType().name(),
                pet.getGender().name(),
                pet.getImageUrl(),
                pet.getImageUrl()
        );

        petCommandService.update(member.getId(), pet.getId(), request, new MockMultipartFile("img", "img".getBytes()));
        Pet findPet = petRepository.getById(pet.getId());

        assertThat(findPet.getName().getValue()).isEqualTo(newName);
    }

    @DisplayName("로그인한 사용자가 반려견의 주인이 아닌 경우, 반려견 정보 수정 요청 시 예외가 발생한다.")
    @Test
    void update_Fail_NotOwner() {
        Member owner = memberRepository.save(new Member("owner", "tag", "imageUrl"));
        Member notOwner = memberRepository.save(new Member("notOwner", "tag", "imageUrl"));
        Pet pet = petRepository.save(
                new Pet(owner, "name", "pet", LocalDate.now().minusDays(1L), SizeType.SMALL, Gender.MALE, "imageUrl"));

        String newName = "newName";
        UpdatePetRequest request = new UpdatePetRequest(
                newName,
                pet.getDescription().getValue(),
                pet.getBirthDate().getValue(),
                pet.getSizeType().name(),
                pet.getGender().name(),
                pet.getImageUrl(),
                pet.getImageUrl()
        );

        assertThatThrownBy(() ->
                petCommandService.update(
                        notOwner.getId(),
                        pet.getId(),
                        request,
                        new MockMultipartFile("img", "img".getBytes())
                ))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("자신이 주인인 반려견의 정보만 수정할 수 있습니다.");
    }
}
