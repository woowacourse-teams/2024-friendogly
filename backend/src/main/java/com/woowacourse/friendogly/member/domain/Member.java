package com.woowacourse.friendogly.member.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
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

    private static final int MAX_PET_CAPACITY = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<Pet> pets;

    @Embedded
    private Name name;

    private String tag;

    @Embedded
    private Email email;

    @Builder
    public Member(String name, String tag, String email) {
        this.pets = new ArrayList<>();
        this.name = new Name(name);
        this.tag = tag;
        this.email = new Email(email);
    }

    public void validatePetCapacity() {
        if (MAX_PET_CAPACITY <= this.pets.size()) {
            throw new FriendoglyException(String.format(
                    "강아지는 최대 %d 마리까지만 등록할 수 있습니다.", MAX_PET_CAPACITY));
        }
    }
}
