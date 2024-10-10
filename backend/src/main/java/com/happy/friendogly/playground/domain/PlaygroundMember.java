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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
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

    @Column(name = "message")
    private String message;

    @Column(name = "is_inside", nullable = false)
    private boolean isInside;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    public PlaygroundMember(
            Playground playground,
            Member member,
            String message,
            boolean isInside,
            LocalDateTime exitTime
    ) {
        this.playground = playground;
        this.member = member;
        this.message = message;
        this.isInside = isInside;
        this.exitTime = exitTime;
    }

    public PlaygroundMember(
            Playground playground,
            Member member
    ) {
        this(playground, member, null, false, null);
    }

    public boolean equalsMemberId(Long memberId) {
        return member.getId().equals(memberId);
    }

    public boolean isSamePlayground(Playground playground) {
        return this.playground.getId().equals(playground.getId());
    }
}
