package com.happy.friendogly.playground.service;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.repository.PetRepository;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import com.happy.friendogly.playground.dto.response.FindPlaygroundDetailResponse;
import com.happy.friendogly.playground.dto.response.detail.PlaygroundPetDetail;
import com.happy.friendogly.playground.repository.PlaygroundMemberRepository;
import com.happy.friendogly.playground.repository.PlaygroundRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PlaygroundQueryService {

    private final PlaygroundRepository playgroundRepository;
    private final PlaygroundMemberRepository playgroundMemberRepository;
    private final PetRepository petRepository;

    public PlaygroundQueryService(
            PlaygroundRepository playgroundRepository,
            PlaygroundMemberRepository playgroundMemberRepository,
            PetRepository petRepository
    ) {
        this.playgroundRepository = playgroundRepository;
        this.playgroundMemberRepository = playgroundMemberRepository;
        this.petRepository = petRepository;
    }

    public FindPlaygroundDetailResponse findDetailById(Long callMemberId, Long playgroundId) {
        Playground playground = playgroundRepository.getById(playgroundId);
        List<PlaygroundMember> playgroundMembers = playgroundMemberRepository.findByPlaygroundId(playgroundId);

        int totalPetCount = 0;
        int arrivedPetCount = 0;

        List<PlaygroundPetDetail> playgroundPetDetails = new ArrayList<>();

        for (PlaygroundMember playgroundMember : playgroundMembers) {
            Member member = playgroundMember.getMember();
            boolean isMyPet = member.getId().equals(callMemberId);

            List<Pet> pets = petRepository.findByMemberId(member.getId());
            totalPetCount += pets.size();
            arrivedPetCount += getArrivedPetCount(playgroundMember, pets);

            playgroundPetDetails.addAll(
                    getPlaygroundPetDetails(pets, playgroundMember, isMyPet)
            );
        }

        boolean isParticipating = playgroundMembers.stream()
                .anyMatch(playgroundMember -> playgroundMember.equalsMemberId(callMemberId));

        return new FindPlaygroundDetailResponse(
                playground.getId(),
                totalPetCount,
                arrivedPetCount,
                isParticipating,
                playgroundPetDetails
        );
    }

    private int getArrivedPetCount(PlaygroundMember playgroundMember, List<Pet> pets) {
        if (playgroundMember.isInside()) {
            return pets.size();
        }
        return 0;
    }

    private List<PlaygroundPetDetail> getPlaygroundPetDetails(
            List<Pet> pets,
            PlaygroundMember petOwner,
            boolean isMyPet
    ) {
        return pets.stream()
                .map(pet -> PlaygroundPetDetail.of(
                        petOwner.getId(),
                        pet,
                        petOwner.getMessage(),
                        petOwner.isInside(),
                        isMyPet
                ))
                .toList();
    }
}
