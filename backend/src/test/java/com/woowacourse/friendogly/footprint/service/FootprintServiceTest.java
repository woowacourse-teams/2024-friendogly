package com.woowacourse.friendogly.footprint.service;

import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import com.woowacourse.friendogly.support.ServiceTest;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class FootprintServiceTest extends ServiceTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected FootprintQueryService footprintQueryService;

    @Autowired
    protected FootprintCommandService footprintCommandService;

    protected Member member;

    protected Pet pet;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(
                Member.builder()
                        .name("name1")
                        .email("test@test.com")
                        .build()
        );

        pet = petRepository.save(
                Pet.builder()
                        .member(member)
                        .name("petname1")
                        .description("petdescription1")
                        .birthDate(LocalDate.now().minusYears(1))
                        .sizeType(SizeType.MEDIUM)
                        .gender(Gender.MALE_NEUTERED)
                        .imageUrl("https://picsum.photos/200")
                        .build()
        );
    }
}
