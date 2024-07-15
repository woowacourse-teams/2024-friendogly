package com.woowacourse.friendogly.pet.domain;

import com.woowacourse.friendogly.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    private String name;

    private String description;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private SizeType sizeType;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private boolean isNeutered;

    private String image;

    @Builder
    public Pet(
            Member member,
            String name,
            String description,
            LocalDate birthDate,
            SizeType sizeType,
            Gender gender,
            boolean isNeutered,
            String image
    ) {
        this.member = member;
        this.name = name;
        this.description = description;
        this.birthDate = birthDate;
        this.sizeType = sizeType;
        this.gender = gender;
        this.isNeutered = isNeutered;
        this.image = image;
    }
}
