package com.woowacourse.friendogly.pet.service;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.dto.request.SavePetRequest;
import com.woowacourse.friendogly.pet.dto.response.SavePetResponse;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PetCommandService {

    private final PetRepository petRepository;
    private final MemberRepository memberRepository;

    public PetCommandService(PetRepository petRepository, MemberRepository memberRepository) {
        this.petRepository = petRepository;
        this.memberRepository = memberRepository;
    }

    public SavePetResponse savePet(SavePetRequest request) {
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 Member Id 입니다."));
        Pet pet = request.toEntity(member);
        Pet savedPet = petRepository.save(pet);

        return new SavePetResponse(savedPet);
    }
}
