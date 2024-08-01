package com.woowacourse.friendogly.club.service;


import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.dto.request.SaveClubMemberRequest;
import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.club.dto.response.SaveClubMemberResponse;
import com.woowacourse.friendogly.club.dto.response.SaveClubResponse;
import com.woowacourse.friendogly.club.repository.ClubRepository;
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
        List<Pet> participatingPets = mapToPets(request.participatingPetsId());

        String imageUrl = "";
        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageManager.uploadFile(image);
        }

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

        club.addClubMember(member);
        club.addClubPet(mapToPets(request.participatingPetsId()));

        //TODO : 채팅방 ID 넘기기
        return new SaveClubMemberResponse(1L);
    }

    private List<Pet> mapToPets(List<Long> participatingPetsId) {
        return participatingPetsId.stream()
                .map(petRepository::getById)
                .toList();
    }

    public void deleteClubMember(Long clubId, Long memberId) {
        Club club = clubRepository.getById(clubId);
        Member member = memberRepository.getById(memberId);

        club.removeClubMember(member);
        if (club.isEmpty()) {
            clubRepository.delete(club);
        }
    }
}
