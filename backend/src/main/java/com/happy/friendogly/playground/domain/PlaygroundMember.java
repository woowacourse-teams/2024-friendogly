package com.happy.friendogly.playground.domain;

import com.happy.friendogly.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PlaygroundMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playground_id", nullable = false)
    private Playground playground;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "is_inside", nullable = false)
    private boolean isInside;

    @Column(name = "participate_time", nullable = false)
    private LocalDateTime participateTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    public PlaygroundMember(
            Playground playground,
            Member member,
            String message,
            boolean isInside,
            LocalDateTime participateTime,
            LocalDateTime exitTime
    ) {
        this.playground = playground;
        this.member = member;
        this.message = message;
        this.isInside = isInside;
        this.participateTime = participateTime;
        this.exitTime = exitTime;
    }

    public PlaygroundMember(
            Playground playground,
            Member member
    ) {
        this(playground, member, "", false, LocalDateTime.now(), null);
    }

    public boolean equalsMemberId(Long memberId) {
        return member.getId().equals(memberId);
    }

    public boolean isSamePlayground(Playground playground) {
        return this.playground.getId().equals(playground.getId());
    }

    public boolean hasEverArrived() {
        return this.exitTime != null;
    }

    public boolean hasNeverArrived() {
        return this.exitTime == null;
    }

    public void updateIsInside(boolean isInside) {
        this.isInside = isInside;
    }

    public void updateMessage(String message) {
        this.message = message;
    }

    public void updateExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public boolean isParticipateTimeBefore(LocalDateTime localDateTime) {
        return this.participateTime.isBefore(localDateTime);
    }

    public boolean isExitTimeBefore(LocalDateTime localDateTime) {
        return this.exitTime.isBefore(localDateTime);
    }
}
