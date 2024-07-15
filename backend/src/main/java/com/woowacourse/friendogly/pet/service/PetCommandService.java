package com.woowacourse.friendogly.pet.service;

import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.dto.request.SavePetRequest;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import org.springframework.stereotype.Service;

@Service
public class PetCommandService {

    private final PetRepository petRepository;

    public PetCommandService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Long savePet(SavePetRequest savePetRequest) {
        Pet pet = savePetRequest.toEntity();
        Pet savedPet = petRepository.save(pet);
        return savedPet.getId();
    }
}
