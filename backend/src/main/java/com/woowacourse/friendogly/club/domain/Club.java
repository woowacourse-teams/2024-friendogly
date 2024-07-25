package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

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

    @OneToMany(mappedBy = "club", orphanRemoval = true, cascade = CascadeType.ALL)
    List<ClubMember> clubMembers = new ArrayList<>();

    @OneToMany(mappedBy = "club", orphanRemoval = true, cascade = CascadeType.ALL)
    List<ClubPet> clubPets = new ArrayList<>();

    @Builder
    public Club(
            String title,
            String content,
            String address,
            int memberCapacity,
            Member owner,
            Set<Gender> allowedGenders,
            Set<SizeType> allowedSizes,
            String imageUrl,
            Status status,
            LocalDateTime createdAt
    ) {
        validateOwner(owner);
        this.title = new Title(title);
        this.content = new Content(content);
        this.address = new Address(address);
        this.memberCapacity = new MemberCapacity(memberCapacity);
        this.owner = owner;
        this.allowedGenders = allowedGenders;
        this.allowedSizes = allowedSizes;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
    }


    private void validateOwner(Member owner) {
        if (owner == null) {
            throw new FriendoglyException("모임 방장 정보는 필수 입니다.");
        }
    }

    public static Club create(
            String title,
            String content,
            String address,
            int memberCapacity,
            Member owner,
            Set<Gender> allowedGender,
            Set<SizeType> allowedSize,
            String imageUrl
    ) {
        Club club = Club.builder()
                .title(title)
                .content(content)
                .address(address)
                .memberCapacity(memberCapacity)
                .owner(owner)
                .allowedGenders(allowedGender)
                .allowedSizes(allowedSize)
                .status(Status.OPEN)
                .createdAt(LocalDateTime.now())
                .imageUrl(imageUrl)
                .build();
        club.addClubMember(club.owner);
        return club;
    }

    public void addClubMember(Member newMember) {
        validateAlreadyExists(newMember);
        validateMemberCapacity();
        ClubMember clubMember = ClubMember.create(this, newMember);
        clubMembers.add(clubMember);
        clubMember.updateClub(this);
    }

    private void validateAlreadyExists(Member newMember) {
        if (clubMembers.stream()
                .anyMatch(clubMember -> Objects.equals(clubMember.getMember().getId(), newMember.getId()))
        ) {
            throw new FriendoglyException("이미 참여 중인 모임입니다.");
        }
    }

    private void validateMemberCapacity() {
        if (clubMembers.size() >= memberCapacity.getValue()) {
            throw new FriendoglyException("최대 인원을 초과하여 모임에 참여할 수 없습니다.");
        }
    }

    public void addClubPet(List<Pet> pets) {
        pets.stream()
                .peek(this::validateParticipatePet)
                .map(pet -> new ClubPet(this, pet))
                .peek(clubPet -> clubPet.updateClub(this))
                .forEach(clubPets::add);
    }

    private void validateParticipatePet(Pet pet) {
        if (!allowedGenders.contains(pet.getGender()) || !allowedSizes.contains(pet.getSizeType())) {
            throw new FriendoglyException("모임에 데려갈 수 없는 강아지가 있습니다.");
        }
    }

    public int countClubMember() {
        return clubMembers.size();
    }

    public boolean isEmpty() {
        return clubMembers.isEmpty();
    }

    public void removeClubMember(Member member) {
        ClubMember target = clubMembers.stream()
                .filter(e -> e.getMember().getId().equals(member.getId()))
                .findAny()
                .orElseThrow(() -> new FriendoglyException("참여 중인 모임이 아닙니다."));
        clubMembers.remove(target);
        if (canDelegate(target)) {
            this.owner = clubMembers.get(0).getMember();
        }
        target.updateClub(null);
        removeClubPets(member);
    }

    private boolean canDelegate(ClubMember target) {
        return owner.getId().equals(target.getMember().getId()) && !isEmpty();
    }

    private void removeClubPets(Member member) {
        List<ClubPet> participatingMemberPets = clubPets.stream()
                .filter(clubPet -> clubPet.getPet().getMember().getId().equals(member.getId()))
                .toList();
        clubPets.removeAll(participatingMemberPets);
        participatingMemberPets.forEach(clubPet -> clubPet.updateClub(null));
    }
}
