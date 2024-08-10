package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.pet.domain.Pet;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class ClubPetPk implements Serializable {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    public ClubPetPk(Club club, Pet pet) {
        this.club = club;
        this.pet = pet;
    }

    public void updateClub(Club club) {
        this.club = club;
    }
}
