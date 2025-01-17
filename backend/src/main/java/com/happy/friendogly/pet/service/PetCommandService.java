package com.happy.friendogly.pet.service;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.infra.FileStorageManager;
import com.happy.friendogly.infra.ImageUpdateType;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.pet.dto.request.SavePetRequest;
import com.happy.friendogly.pet.dto.request.UpdatePetRequest;
import com.happy.friendogly.pet.dto.response.SavePetResponse;
import com.happy.friendogly.pet.repository.PetRepository;
import org.springframework.http.HttpStatus;
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

    public void update(Long memberId, Long petId, UpdatePetRequest request, MultipartFile newImage) {
        Member member = memberRepository.getById(memberId);
        Pet pet = petRepository.getById(petId);

        if (!pet.isOwner(member)) {
            throw new FriendoglyException("자신의 강아지만 수정할 수 있습니다.", HttpStatus.FORBIDDEN);
        }

        ImageUpdateType imageUpdateType = ImageUpdateType.from(request.imageUpdateType());
        String oldImageUrl = pet.getImageUrl();
        String newImageUrl = fileStorageManager.updateFile(oldImageUrl, newImage, imageUpdateType);

        pet.update(
                request.name(),
                request.description(),
                request.birthDate(),
                request.sizeType(),
                request.gender(),
                newImageUrl
        );
    }
}
