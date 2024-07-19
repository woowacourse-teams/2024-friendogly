package com.woowacourse.friendogly.club.domain;

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
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
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

    @Embedded
    private MemberCapacity memberCapacity;

    @OneToOne(optional = false)
    private Member owner;

    //TODO: 필터링 조건

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
            Status status
    ) {
        validateOwner(owner);
        this.title = new Title(title);
        this.content = new Content(content);
        this.memberCapacity = new MemberCapacity(memberCapacity);
        this.owner = owner;
        this.imageUrl = imageUrl;
        this.status = status;
        createdAt = LocalDateTime.now();
    }

    private void validateOwner(Member owner) {
        if (owner == null) {
            throw new FriendoglyException("모임 방장 정보는 필수 입니다.");
        }
    }
}
