package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Pet;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @Column(nullable = false)
    private MemberCapacity memberCapacity;

    @OneToOne(optional = false)
    private Member owner;

    @ManyToMany
    private List<Member> participantMembers = new ArrayList<>();

    @ManyToMany
    private List<Pet> participantPets = new ArrayList<>();

    //TODO: 필터링 조건

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Club(
            String title,
            String content,
            int memberCapacity,
            Member owner,
            String imageUrl,
            List<Pet> participantPets,
            Status status
    ) {
        this.title = new Title(title);
        this.content = new Content(content);
        this.memberCapacity = new MemberCapacity(memberCapacity);
        this.owner = owner;
        this.imageUrl = imageUrl;
        this.participantPets = participantPets;
        this.status = status;
        createdAt = LocalDateTime.now();
        participantMembers.add(owner);
    }

    public int getNumberOfParticipant() {
        return participantMembers.size();
    }

    public void addParticipant(Member member) {
        participantMembers.add(member);
    }

    public void removeParticipant(Member member) {
        participantMembers.remove(member);
    }
}
