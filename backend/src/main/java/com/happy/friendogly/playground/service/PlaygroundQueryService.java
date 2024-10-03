package com.happy.friendogly.playground.service;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.repository.PetRepository;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import com.happy.friendogly.playground.dto.response.FindPlaygroundDetailResponse;
import com.happy.friendogly.playground.dto.response.FindPlaygroundLocationResponse;
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

    public FindPlaygroundDetailResponse findDetail(Long callMemberId, Long playgroundId) {
        Playground playground = playgroundRepository.getById(playgroundId);
        List<PlaygroundMember> playgroundMembers = playgroundMemberRepository.findAllByPlaygroundId(playgroundId);

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
                    PlaygroundPetDetail.getListOf(pets, playgroundMember, isMyPet)
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

    public List<FindPlaygroundLocationResponse> findLocations(Long memberId) {
        List<Playground> playgrounds = playgroundRepository.findAll();
        List<FindPlaygroundLocationResponse> playgroundLocationResponses = new ArrayList<>();
        for (Playground playground : playgrounds) {
            List<PlaygroundMember> playgroundMembers = playgroundMemberRepository
                    .findAllByPlaygroundId(playground.getId());
            boolean isParticipating = playgroundMembers.stream()
                    .anyMatch(playgroundMember -> playgroundMember.equalsMemberId(memberId));
            playgroundLocationResponses.add(
                    new FindPlaygroundLocationResponse(
                            playground.getId(),
                            playground.getLocation().getLatitude(),
                            playground.getLocation().getLongitude(),
                            isParticipating
                    )
            );
        }
        return playgroundLocationResponses;
    }
}
