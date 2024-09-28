package com.happy.friendogly.playground.domain;

import com.happy.friendogly.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PlaygroundMember {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playground_id", nullable = false)
    private Playground playground;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "message")
    private String message;

    @Column(name = "isInside", nullable = false)
    private boolean isInside;

    @Column(name = "exitTime")
    private LocalDateTime exitTime;

    public PlaygroundMember(
            Long id,
            Playground playground,
            Member member,
            String message,
            boolean isInside,
            LocalDateTime exitTime
    ) {
        this.id = id;
        this.playground = playground;
        this.member = member;
        this.message = message;
        this.isInside = isInside;
        this.exitTime = exitTime;
    }
}
