package com.woowacourse.friendogly.pet.service;

import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.dto.response.FindPetResponse;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import org.springframework.stereotype.Service;

@Service
public class PetQueryService {

    private final PetRepository petRepository;

    public PetQueryService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public FindPetResponse findPet(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Pet Id입니다."));
        return FindPetResponse.from(pet);
    }
}
