package com.happy.friendogly.playground.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.playground.domain.Location;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PlaygroundRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PlaygroundRepository playgroundRepository;

    @Autowired
    PlaygroundMemberRepository playgroundMemberRepository;

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
        assertThat(playgrounds).hasSize(1);
    }

    @DisplayName("참여한 멤버가 존재하지 않는 놀이터를 여러개 함께 삭제할 수 있다.")
    @Test
    void deleteAllHasNotMemberByIdIn() {
        // given
        Member member = memberRepository.save(new Member("김도선", "tag", "imageUrl"));
        Playground hasNotMemberPlayground = playgroundRepository.save(
                new Playground(new Location(10, 10))
        );
        Playground hasMemberPlayground = playgroundRepository.save(
                new Playground(new Location(10, 12))
        );
        playgroundMemberRepository.save(
                new PlaygroundMember(
                        hasMemberPlayground,
                        member
                )
        );

        // given
        playgroundRepository.deleteAllHasNotMemberByIdIn(
                List.of(hasMemberPlayground.getId(), hasNotMemberPlayground.getId()));

        // when
        assertAll(
                () -> assertThat(playgroundRepository.findAll()).hasSize(1),
                () -> assertThat(playgroundRepository.existsById(hasNotMemberPlayground.getId())).isFalse()
        );
    }
}

