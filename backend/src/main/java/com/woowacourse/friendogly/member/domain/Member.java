package com.woowacourse.friendogly.member.domain;

import com.woowacourse.friendogly.pet.domain.Pet;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<Pet> pets;

    @Embedded
    private Name name;

    @Embedded
    private Email email;

    @Builder
    public Member(String name, String email) {
        this.pets = new ArrayList<>();
        this.name = new Name(name);
        this.email = new Email(email);
    }
}
