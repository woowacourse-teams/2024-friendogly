package com.happy.friendogly.member.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    private String tag;

    private String imageUrl;

    @Builder
    public Member(String name, String tag, String imageUrl) {
        this.name = new Name(name);
        this.tag = tag;
        this.imageUrl = imageUrl;
    }

    public void update(String name, String imageUrl) {
        this.name = new Name(name);
        this.imageUrl = imageUrl;
    }
}
