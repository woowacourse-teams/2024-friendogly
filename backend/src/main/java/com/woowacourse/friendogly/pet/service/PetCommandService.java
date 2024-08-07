package com.woowacourse.friendogly.pet.service;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.infra.FileStorageManager;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import com.woowacourse.friendogly.pet.dto.request.SavePetRequest;
import com.woowacourse.friendogly.pet.dto.response.SavePetResponse;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class PetCommandService {

    private static final int MAX_PET_CAPACITY = 5;

    private final FileStorageManager fileStorageManager;
    private final PetRepository petRepository;
    private final MemberRepository memberRepository;

    public PetCommandService(
            FileStorageManager fileStorageManager,
            PetRepository petRepository,
            MemberRepository memberRepository
    ) {
        this.fileStorageManager = fileStorageManager;
        this.petRepository = petRepository;
        this.memberRepository = memberRepository;
    }

    public SavePetResponse savePet(Long memberId, SavePetRequest request, MultipartFile image) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 Member 입니다."));

        validatePetCapacity(petRepository.countByMember(member));

        String imageUrl = "";
        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageManager.uploadFile(image);
        }

        Pet savedPet = petRepository.save(Pet.builder()
                .member(member)
                .name(request.name())
                .description(request.description())
                .birthDate(request.birthDate())
                .sizeType(SizeType.toSizeType(request.sizeType()))
                .gender(Gender.toGender(request.gender()))
                .imageUrl(imageUrl)
                .build());

        return new SavePetResponse(savedPet);
    }

    private void validatePetCapacity(Long size) {
        if (MAX_PET_CAPACITY <= size) {
            throw new FriendoglyException(String.format(
                    "강아지는 최대 %d 마리까지만 등록할 수 있습니다.", MAX_PET_CAPACITY));
        }
    }
}
