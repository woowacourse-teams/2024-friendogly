package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.domain.Name;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @Embedded
    private MemberCapacity memberCapacity;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "club_gender", joinColumns = @JoinColumn(name = "club_id"))
    @Column(name = "allowed_gender", nullable = false)
    private Set<Gender> allowedGenders;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "club_size", joinColumns = @JoinColumn(name = "club_id"))
    @Column(name = "allowed_size", nullable = false)
    private Set<SizeType> allowedSizes;

    @Embedded
    private Address address;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @OneToMany(mappedBy = "clubMemberPk.club", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ClubMember> clubMembers = new ArrayList<>();

    @OneToMany(mappedBy = "clubPetPk.club", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ClubPet> clubPets = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private ChatRoom chatRoom;

    @Builder
    private Club(
            String title,
            String content,
            String province,
            String city,
            String village,
            int memberCapacity,
            Set<Gender> allowedGenders,
            Set<SizeType> allowedSizes,
            String imageUrl,
            Status status,
            LocalDateTime createdAt,
            ChatRoom chatRoom
    ) {
        this.title = new Title(title);
        this.content = new Content(content);
        this.address = new Address(province, city, village);
        this.memberCapacity = new MemberCapacity(memberCapacity);
        this.allowedGenders = allowedGenders;
        this.allowedSizes = allowedSizes;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.chatRoom = chatRoom;
    }

    public static Club create(
            String title,
            String content,
            String province,
            String city,
            String village,
            int memberCapacity,
            Member owner,
            Set<Gender> allowedGender,
            Set<SizeType> allowedSize,
            String imageUrl,
            List<Pet> participatingPets
    ) {
        Club club = Club.builder()
                .title(title)
                .content(content)
                .province(province)
                .city(city)
                .village(village)
                .memberCapacity(memberCapacity)
                .allowedGenders(allowedGender)
                .allowedSizes(allowedSize)
                .status(Status.OPEN)
                .createdAt(LocalDateTime.now())
                .imageUrl(imageUrl)
                .chatRoom(ChatRoom.createGroup(memberCapacity))
                .build();
        club.addClubMember(owner);
        club.addClubPet(participatingPets);
        club.addChatRoomMember(owner);
        return club;
    }

    public void addChatRoomMember(Member member) {
        this.chatRoom.addMember(member);
    }

    public void removeChatRoomMember(Member member) {
        this.chatRoom.removeMember(member);
    }

    public void addClubMember(Member member) {
        validateAlreadyExists(member);
        validateMemberCapacity();

        ClubMember clubMember = ClubMember.create(this, member);
        clubMembers.add(clubMember);
    }

    private void validateAlreadyExists(Member member) {
        if (isAlreadyJoined(member)) {
            throw new FriendoglyException("이미 참여 중인 모임입니다.");
        }
    }

    public boolean isAlreadyJoined(Member member) {
        return clubMembers.stream()
                .anyMatch(clubMember -> clubMember.isSameMember(member));
    }

    private void validateMemberCapacity() {
        if (memberCapacity.isCapacityReached(countClubMember())) {
            throw new FriendoglyException("최대 인원을 초과하여 모임에 참여할 수 없습니다.");
        }
    }

    public void addClubPet(List<Pet> pets) {
        List<ClubPet> clubPets = pets.stream()
                .peek(this::validateParticipatePet)
                .map(pet -> new ClubPet(this, pet))
                .toList();
        this.clubPets.addAll(clubPets);
    }

    private void validateParticipatePet(Pet pet) {
        if (!canJoinWith(pet)) {
            throw new FriendoglyException("모임에 데려갈 수 없는 강아지가 있습니다.");
        }
    }

    private boolean canJoinWith(Pet pet) {
        return allowedGenders.contains(pet.getGender()) && allowedSizes.contains(pet.getSizeType());
    }

    public boolean isJoinable(Member member, List<Pet> pets) {
        boolean hasJoinablePet = pets.stream()
                .anyMatch(this::canJoinWith);
        boolean isNotFull = !this.memberCapacity.isCapacityReached(countClubMember());

        return hasJoinablePet && isNotFull && !isAlreadyJoined(member) && isOpen();
    }

    public void removeClubMember(Member member) {
        ClubMember targetClubMember = findTargetClubMember(member);
        clubMembers.remove(targetClubMember);
        targetClubMember.updateClub(null);
        removeClubPets(member);
    }

    private ClubMember findTargetClubMember(Member member) {
        return clubMembers.stream()
                .filter(currentClubMember -> currentClubMember.isSameMember(member))
                .findAny()
                .orElseThrow(() -> new FriendoglyException("참여 중인 모임이 아닙니다."));
    }

    private void removeClubPets(Member member) {
        List<ClubPet> participatingMemberPets = findTargetClubPets(member);
        clubPets.removeAll(participatingMemberPets);
        participatingMemberPets.forEach(clubPet -> clubPet.updateClub(null));
    }

    private List<ClubPet> findTargetClubPets(Member member) {
        return clubPets.stream()
                .filter(clubPet -> clubPet.isSameMember(member))
                .toList();
    }

    public int countClubMember() {
        return clubMembers.size();
    }

    public boolean isEmpty() {
        return clubMembers.isEmpty();
    }

    public boolean isOpen() {
        return this.status == Status.OPEN;
    }

    public boolean isOwner(Member targetMember) {
        return findOwner().isSameMember(targetMember);
    }

    public Name findOwnerName() {
        return findOwner().getClubMemberPk().getMember().getName();
    }

    public String findOwnerImageUrl() {
        return findOwner().getClubMemberPk().getMember().getImageUrl();
    }

    private ClubMember findOwner() {
        return clubMembers.stream()
                .min(Comparator.comparing(ClubMember::getCreatedAt))
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 모임입니다."));
    }
}
