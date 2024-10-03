package com.happy.friendogly.playground.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.happy.friendogly.playground.domain.Location;
import com.happy.friendogly.playground.domain.Playground;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PlaygroundRepositoryTest {

    @Autowired
    PlaygroundRepository playgroundRepository;

    @DisplayName("주어진 위도, 경도 사이에 있는 놀이터를 찾을 수 있다.")
    @Test
    void findAllByLatitudeBetweenAndLongitudeBetween() {
        // given
        playgroundRepository.save(
                new Playground(new Location(10, 10))
        );
        playgroundRepository.save(
                new Playground(new Location(10, 12))
        );

        // when
        List<Playground> playgrounds = playgroundRepository
                .findAllByLocation_LatitudeBetweenAndLocation_LongitudeBetween(9, 11, 9, 11);

        // then
        assertThat(playgrounds.size()).isOne();
    }

}
