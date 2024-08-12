package com.happy.friendogly.footprint.service;

import static com.happy.friendogly.footprint.domain.WalkStatus.AFTER;
import static com.happy.friendogly.footprint.domain.WalkStatus.BEFORE;
import static com.happy.friendogly.footprint.domain.WalkStatus.ONGOING;

import com.happy.friendogly.footprint.domain.Footprint;
import com.happy.friendogly.footprint.domain.Location;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.support.ServiceTest;
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

    protected Footprint FOOTPRINT_STATUS_BEFORE(Location location) {
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

    protected Footprint FOOTPRINT_STATUS_ONGOING(Location location) {
        return new Footprint(
                member,
                location,
                ONGOING,
                LocalDateTime.now(),
                null,
                LocalDateTime.now().minusHours(1),
                false
        );
    }

    protected Footprint FOOTPRINT_STATUS_ONGOING(LocalDateTime createdAt) {
        return new Footprint(
                member,
                new Location(0, 0),
                BEFORE,
                LocalDateTime.now(),
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
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now(),
                LocalDateTime.now().minusHours(2),
                false
        );
    }

    protected Footprint FOOTPRINT_STATUS_AFTER(LocalDateTime createdAt) {
        return new Footprint(
                member,
                new Location(0, 0),
                AFTER,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now(),
                createdAt,
                false
        );
    }

    protected static double ONE_METER_LOCATION_UNIT = 0.0000089847;

    protected static double LONGITUDE_WITH_METER_FROM_ZERO(double meter) {
        return meter * ONE_METER_LOCATION_UNIT;
    }
}
