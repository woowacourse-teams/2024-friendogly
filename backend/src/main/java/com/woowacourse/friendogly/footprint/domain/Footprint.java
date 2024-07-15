package com.woowacourse.friendogly.footprint.domain;

import com.woowacourse.friendogly.member.domain.Member;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Footprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @Embedded
    private Location location;

    private LocalDateTime createdAt;

    private boolean isDeleted;

    @Builder
    public Footprint(Member member, Location location) {
        this.member = member;
        this.location = location;
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }
}
