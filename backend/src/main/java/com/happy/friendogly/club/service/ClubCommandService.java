package com.happy.friendogly.club.service;


import com.happy.friendogly.chat.service.ChatCommandService;
import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.dto.request.DeleteKickedMemberRequest;
import com.happy.friendogly.club.dto.request.SaveClubMemberRequest;
import com.happy.friendogly.club.dto.request.SaveClubRequest;
import com.happy.friendogly.club.dto.request.UpdateClubRequest;
import com.happy.friendogly.club.dto.response.SaveClubMemberResponse;
import com.happy.friendogly.club.dto.response.SaveClubResponse;
import com.happy.friendogly.club.dto.response.UpdateClubResponse;
import com.happy.friendogly.club.repository.ClubRepository;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.infra.FileStorageManager;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.pet.repository.PetRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
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
    private final ChatCommandService chatCommandService;

    public ClubCommandService(
            ClubRepository clubRepository,
            MemberRepository memberRepository,
            PetRepository petRepository,
            FileStorageManager fileStorageManager,
            ChatCommandService chatCommandService
    ) {
        this.clubRepository = clubRepository;
        this.memberRepository = memberRepository;
        this.petRepository = petRepository;
        this.fileStorageManager = fileStorageManager;
        this.chatCommandService = chatCommandService;
    }

    public SaveClubResponse save(Long memberId, MultipartFile image, SaveClubRequest request) {
        Member member = memberRepository.getById(memberId);
        List<Pet> participatingPets = mapToPets(request.participatingPetsId(), member);

        String imageUrl = "";
        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageManager.uploadFile(image);
        }

        Club newClub = Club.create(
                request.title(),
                request.content(),
                request.province(),
                request.city(),
                request.village(),
                request.memberCapacity(),
                member,
                Gender.toGenders(request.allowedGenders()),
                SizeType.toSizeTypes(request.allowedSizes()),
                imageUrl,
                participatingPets
        );

        return new SaveClubResponse(clubRepository.save(newClub));
    }

    public SaveClubMemberResponse joinClub(Long clubId, Long memberId, SaveClubMemberRequest request) {
        Club club = clubRepository.getById(clubId);
        Member member = memberRepository.getById(memberId);

        club.addClubMember(member);
        club.addClubPet(mapToPets(request.participatingPetsId(), member));
        club.addChatRoomMember(member);

        chatCommandService.sendEnter(memberId, club.getChatRoom().getId());

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

        club.removeClubMember(member);
        club.removeChatRoomMember(member);
        if (club.isEmpty()) {
            clubRepository.delete(club);
        }
    }

    public UpdateClubResponse update(Long clubId, Long memberId, UpdateClubRequest request) {
        Club club = clubRepository.getById(clubId);
        Member member = memberRepository.getById(memberId);
        if (club.isOwner(member)) {
            club.update(request.title(), request.content(), request.status());
            return new UpdateClubResponse(club);
        }
        throw new FriendoglyException("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    public void kickMember(Long clubId, Long memberId, DeleteKickedMemberRequest request) {
        Club club = clubRepository.getById(clubId);
        Member member = memberRepository.getById(memberId);
        Member kickedMember = memberRepository.getById(request.kickedMemberId());
        validateKick(club, member, kickedMember);
        club.removeClubMember(kickedMember);
        club.removeChatRoomMember(member);
    }

    private void validateKick(Club club, Member owner, Member memberToKick) {
        if (!club.isOwner(owner)) {
            throw new FriendoglyException("강퇴 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        if (club.isOwner(memberToKick)) {
            throw new FriendoglyException("자기 자신은 강퇴할 수 없습니다.");
        }
    }
}
