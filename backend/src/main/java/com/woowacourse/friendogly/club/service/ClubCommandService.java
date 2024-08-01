package com.woowacourse.friendogly.club.service;


import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.dto.request.SaveClubMemberRequest;
import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.club.dto.response.SaveClubMemberResponse;
import com.woowacourse.friendogly.club.dto.response.SaveClubResponse;
import com.woowacourse.friendogly.club.repository.ClubRepository;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.infra.FileStorageManager;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ClubCommandService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final FileStorageManager fileStorageManager;

    public ClubCommandService(
            ClubRepository clubRepository,
            MemberRepository memberRepository,
            PetRepository petRepository,
            FileStorageManager fileStorageManager
    ) {
        this.clubRepository = clubRepository;
        this.memberRepository = memberRepository;
        this.petRepository = petRepository;
        this.fileStorageManager = fileStorageManager;
    }

    public SaveClubResponse save(Long memberId, MultipartFile image, SaveClubRequest request) {
        Member member = memberRepository.getById(memberId);
        List<Pet> participatingPets = mapToPets(request.participatingPetsId(), member);
        String imageUrl = fileStorageManager.uploadFile(image);

        Club newClub = Club.create(
                request.title(),
                request.content(),
                request.address(),
                request.memberCapacity(),
                member,
                request.allowedGenders(),
                request.allowedSizes(),
                imageUrl,
                participatingPets
        );

        return new SaveClubResponse(clubRepository.save(newClub));
    }

    public SaveClubMemberResponse joinClub(Long clubId, Long memberId, SaveClubMemberRequest request) {
        Club club = clubRepository.getById(clubId);
        Member member = memberRepository.getById(memberId);

        List<Pet> pets = mapToPets(request.participatingPetsId(), member);
        club.addMember(member, pets);

        return new SaveClubMemberResponse(memberId, club.getChatRoom().getId());
    }

    private List<Pet> mapToPets(List<Long> participatingPetsId, Member member) {
        List<Pet> participatingPets = participatingPetsId.stream()
                .map(petRepository::getById)
                .toList();
        boolean isNotOwner = participatingPets.stream()
                .anyMatch(pet -> !pet.getMember().getId().equals(member.getId()));

        if (isNotOwner) {
            throw new FriendoglyException("자신의 반려견만 모임에 데려갈 수 있습니다.");
        }
        return participatingPets;
    }

    public void deleteClubMember(Long clubId, Long memberId) {
        Club club = clubRepository.getById(clubId);
        Member member = memberRepository.getById(memberId);

        club.removeMember(member);
        if (club.isEmpty()) {
            clubRepository.delete(club);
        }
    }
}
