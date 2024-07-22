package com.woowacourse.friendogly.footprint.domain;

import com.woowacourse.friendogly.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Footprint {

    private static final int RADIUS_AS_METER = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    @Column(nullable = false)
    private Location location;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public Footprint(Member member, Location location) {
        this.member = member;
        this.location = location;
        this.imageUrl = "";
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    public boolean isNear(Location location) {
        return this.location.isWithin(location, RADIUS_AS_METER);
    }

    public boolean isCreatedBy(Long memberId) {
        return this.member.getId()
                .equals(memberId);
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
