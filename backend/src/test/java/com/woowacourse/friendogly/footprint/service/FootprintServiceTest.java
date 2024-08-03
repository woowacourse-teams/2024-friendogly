package com.woowacourse.friendogly.footprint.service;

import static com.woowacourse.friendogly.footprint.domain.WalkStatus.AFTER;
import static com.woowacourse.friendogly.footprint.domain.WalkStatus.BEFORE;
import static com.woowacourse.friendogly.footprint.domain.WalkStatus.ONGOING;

import com.woowacourse.friendogly.footprint.domain.Footprint;
import com.woowacourse.friendogly.footprint.domain.Location;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import com.woowacourse.friendogly.support.ServiceTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    //FIXTURE

    protected Footprint FOOTPRINT() {
        return new Footprint(member, new Location(0, 0));
    }

    protected Footprint FOOTPRINT_DELETED() {
        return new Footprint(member,
                new Location(0, 0),
                BEFORE,
                null,
                null,
                LocalDateTime.now(),
                true);
    }

    protected Footprint FOOTPRINT(LocalDateTime createdAt) {
        return new Footprint(
                member,
                new Location(0, 0),
                BEFORE,
                null,
                null,
                createdAt,
                false
        );
    }
    protected Footprint FOOTPRINT_STATUS_BEFORE(Location location){
        return new Footprint(
                member,
                location,
                BEFORE,
                null,
                null,
                LocalDateTime.now(),
                false
        );
    }
    protected Footprint FOOTPRINT_STATUS_BEFORE(LocalDateTime createdAt) {
        return new Footprint(
                member,
                new Location(0, 0),
                BEFORE,
                null,
                null,
                createdAt,
                false
        );
    }

    protected Footprint FOOTPRINT_STATUS_ONGOING(Location location){
        return new Footprint(
                member,
                location,
                ONGOING,
                null,
                null,
                LocalDateTime.now(),
                false
        );
    }

    protected Footprint FOOTPRINT_STATUS_ONGOING(LocalDateTime createdAt) {
        return new Footprint(
                member,
                new Location(0, 0),
                BEFORE,
                null,
                null,
                createdAt,
                false
        );
    }

    protected Footprint FOOTPRINT_STATUS_AFTER(Location location) {
        return new Footprint(
                member,
                location,
                AFTER,
                null,
                null,
                LocalDateTime.now(),
                false
        );
    }

    protected Footprint FOOTPRINT_STATUS_AFTER(LocalDateTime createdAt) {
        return new Footprint(
                member,
                new Location(0, 0),
                AFTER,
                null,
                null,
                createdAt,
                false
        );
    }
}
