package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
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
import java.time.LocalDateTime;
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
        return Club.builder()
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
    }

}
