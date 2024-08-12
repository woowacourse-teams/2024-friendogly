package com.happy.friendogly.pet.service;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.dto.response.FindPetResponse;
import com.happy.friendogly.pet.repository.PetRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PetQueryService {

    private final PetRepository petRepository;

    public PetQueryService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public FindPetResponse findById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 Pet입니다."));
        return new FindPetResponse(pet);
    }

    public List<FindPetResponse> findByMemberId(Long memberId) {
        List<Pet> pets = petRepository.findByMemberId(memberId);
        return pets.stream()
                .map(FindPetResponse::new)
                .toList();
    }
}
