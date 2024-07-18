package com.woowacourse.friendogly.pet.service;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.dto.response.FindPetResponse;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PetQueryService {

    private final PetRepository petRepository;

    public PetQueryService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public FindPetResponse findPet(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 Pet입니다."));
        return new FindPetResponse(pet);
    }

    public List<FindPetResponse> findPets(Long memberId) {
        List<Pet> pets = petRepository.findByMemberId(memberId);
        return pets.stream()
                .map(FindPetResponse::new)
                .toList();
    }
}
