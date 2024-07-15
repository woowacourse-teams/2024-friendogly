package com.woowacourse.friendogly.pet.service;

import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.dto.request.SavePetRequest;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import org.springframework.stereotype.Service;

@Service
public class PetCommandService {

    private final PetRepository petRepository;
    private final MemberRepository memberRepository;

    public PetCommandService(PetRepository petRepository, MemberRepository memberRepository) {
        this.petRepository = petRepository;
        this.memberRepository = memberRepository;
    }

    public Long savePet(SavePetRequest savePetRequest) {
        Member member = memberRepository.findById(savePetRequest.memberId())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 Member Id 입니다."));
        Pet pet = savePetRequest.toEntity(member);
        Pet savedPet = petRepository.save(pet);
        return savedPet.getId();
    }
}
