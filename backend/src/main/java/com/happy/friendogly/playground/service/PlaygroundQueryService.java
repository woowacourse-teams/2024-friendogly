package com.happy.friendogly.playground.service;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.repository.PetRepository;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import com.happy.friendogly.playground.dto.response.FindMyPlaygroundLocation;
import com.happy.friendogly.playground.dto.response.FindPlaygroundDetailResponse;
import com.happy.friendogly.playground.dto.response.FindPlaygroundLocationResponse;
import com.happy.friendogly.playground.dto.response.FindPlaygroundSummaryResponse;
import com.happy.friendogly.playground.dto.response.detail.PlaygroundPetDetail;
import com.happy.friendogly.playground.repository.PlaygroundMemberRepository;
import com.happy.friendogly.playground.repository.PlaygroundRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PlaygroundQueryService {

    private static final int MAX_PET_PREVIEW_IMAGE_COUNT = 5;

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

        playgroundPetDetails.sort(Comparator
                .comparing(PlaygroundPetDetail::isMine).reversed()
                .thenComparing(Comparator.comparing(PlaygroundPetDetail::isArrival).reversed())
        );

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
        Optional<PlaygroundMember> playgroundMember = playgroundMemberRepository.findByMemberId(memberId);

        if (playgroundMember.isPresent()) {
            return playgrounds.stream()
                    .map(playground -> new FindPlaygroundLocationResponse(
                            playground,
                            playgroundMember.get().isSamePlayground(playground)
                    )).toList();
        }

        return playgrounds.stream()
                .map(playground -> new FindPlaygroundLocationResponse(playground, false))
                .toList();
    }

    public FindPlaygroundSummaryResponse findSummary(Long playgroundId) {
        List<PlaygroundMember> playgroundMembers = playgroundMemberRepository
                .findAllByPlaygroundIdOrderByIsInsideDesc(playgroundId);

        int totalPetCount = 0;
        int arrivedPetCount = 0;
        List<String> petImages = new ArrayList<>();

        for (PlaygroundMember playgroundMember : playgroundMembers) {
            List<Pet> pets = petRepository.findByMemberId(playgroundMember.getMember().getId());

            totalPetCount += pets.size();
            arrivedPetCount += getArrivedPetCount(playgroundMember, pets);
            petImages.addAll(
                    pets.stream()
                            .map(Pet::getImageUrl)
                            .toList()
            );
        }

        petImages = cutPetImagesCount(petImages);

        return new FindPlaygroundSummaryResponse(
                playgroundId,
                totalPetCount,
                arrivedPetCount,
                petImages
        );
    }

    private List<String> cutPetImagesCount(List<String> petImageUrls) {
        if (petImageUrls.size() > MAX_PET_PREVIEW_IMAGE_COUNT) {
            return petImageUrls.subList(0, MAX_PET_PREVIEW_IMAGE_COUNT);
        }
        return petImageUrls;
    }

    public FindMyPlaygroundLocation findMyPlaygroundLocation(Long memberId) {
        PlaygroundMember playgroundMember = playgroundMemberRepository.getByMemberId(memberId);
        Playground playground = playgroundMember.getPlayground();
        return new FindMyPlaygroundLocation(
                playground.getId(),
                playground.getLocation().getLatitude(),
                playground.getLocation().getLongitude()
        );
    }
}
