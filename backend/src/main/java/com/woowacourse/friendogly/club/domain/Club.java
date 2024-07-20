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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "club_gender", joinColumns = @JoinColumn(name = "club_id"))
    @Column(name = "allowed_gender")
    private List<Gender> allowedGender;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "club_size", joinColumns = @JoinColumn(name = "club_id"))
    @Column(name = "allowed_size")
    private List<SizeType> allowedSize;

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
            Address address,
            int memberCapacity,
            Member owner,
            List<Gender> allowedGender,
            List<SizeType> allowedSize,
            String imageUrl,
            Status status,
            LocalDateTime createdAt
    ) {
        validateClubLocation(address);
        validateOwner(owner);
        this.title = new Title(title);
        this.content = new Content(content);
        this.address = address;
        this.memberCapacity = new MemberCapacity(memberCapacity);
        this.owner = owner;
        this.allowedGender = allowedGender;
        this.allowedSize = allowedSize;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
    }

    private void validateClubLocation(Address address) {
        if (address == null) {
            throw new FriendoglyException("모임 위치 정보는 필수입니다.");
        }
    }

    private void validateOwner(Member owner) {
        if (owner == null) {
            throw new FriendoglyException("모임 방장 정보는 필수 입니다.");
        }
    }
}
