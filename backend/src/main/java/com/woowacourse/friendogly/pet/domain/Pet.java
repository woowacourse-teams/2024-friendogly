package com.woowacourse.friendogly.pet.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @Embedded
    private Name name;

    @Embedded
    private Description description;

    @Embedded
    private BirthDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SizeType sizeType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private String imageUrl;

    @Builder
    public Pet(
            Member member,
            String name,
            String description,
            LocalDate birthDate,
            SizeType sizeType,
            Gender gender,
            String imageUrl
    ) {
        validateMember(member);
        
        this.member = member;
        this.name = new Name(name);
        this.description = new Description(description);
        this.birthDate = new BirthDate(birthDate);
        this.sizeType = sizeType;
        this.gender = gender;
        this.imageUrl = imageUrl;
    }

    private void validateMember(Member member) {
        if (member == null) {
            throw new FriendoglyException("member는 null일 수 없습니다.");
        }
    }

    public boolean isOwner(Member member) {
        return this.member.getId().equals(member.getId());
    }
}
