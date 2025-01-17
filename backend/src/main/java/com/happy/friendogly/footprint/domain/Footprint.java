package com.happy.friendogly.footprint.domain;

import static com.happy.friendogly.footprint.domain.WalkStatus.AFTER;
import static com.happy.friendogly.footprint.domain.WalkStatus.ONGOING;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Footprint {

    private static final int NEAR_RADIUS_AS_METER = 400_000;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "walk_status")
    private WalkStatus walkStatus;

    @Column(name = "start_walk_time")
    private LocalDateTime startWalkTime;

    @Column(name = "end_walk_time")
    private LocalDateTime endWalkTime;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public Footprint(
            Member member,
            Location location
    ) {
        this(
                member,
                location,
                WalkStatus.BEFORE,
                null,
                null,
                LocalDateTime.now(),
                false
        );
    }

    public Footprint(
            Member member,
            Location location,
            WalkStatus walkStatus,
            LocalDateTime startWalkTime,
            LocalDateTime endWalkTime,
            LocalDateTime createdAt,
            boolean isDeleted
    ) {
        this.member = member;
        this.location = location;
        this.walkStatus = walkStatus;
        this.startWalkTime = startWalkTime;
        this.endWalkTime = endWalkTime;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public boolean isNear(Location location) {
        return this.location.isWithin(location, NEAR_RADIUS_AS_METER);
    }

    public boolean isInsideBoundary(Location location) {
        return this.location.isWithin(location, RADIUS_AS_METER);
    }

    public boolean isCreatedBy(Long memberId) {
        return this.member.getId()
                .equals(memberId);
    }

    public void updateToDeleted() {
        isDeleted = true;
    }

    public LocalDateTime findChangedWalkStatusTime() {
        if (walkStatus.isBefore()) {
            return createdAt;
        }
        if (walkStatus.isOngoing()) {
            return startWalkTime;
        }
        return endWalkTime;
    }

    public void updateWalkStatusWithCurrentLocation(Location currentLocation) {
        if (walkStatus.isBefore() && isInsideBoundary(currentLocation)) {
            startWalk();
        }
        if (walkStatus.isOngoing() && !isInsideBoundary(currentLocation)) {
            endWalk();
        }
    }

    public void finishWalkingManual() {
        if (walkStatus.isBefore() || walkStatus.isAfter()) {
            throw new FriendoglyException("산책 중에서 산책 후로의 변경만 가능합니다.");
        }
        endWalk();
    }

    private void startWalk() {
        walkStatus = ONGOING;
        startWalkTime = LocalDateTime.now();
    }

    private void endWalk() {
        walkStatus = AFTER;
        endWalkTime = LocalDateTime.now();
    }
}
