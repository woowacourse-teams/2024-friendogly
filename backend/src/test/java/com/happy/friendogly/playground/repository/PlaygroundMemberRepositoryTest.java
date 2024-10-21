package com.happy.friendogly.playground.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.playground.domain.Location;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PlaygroundMemberRepositoryTest {

    @Autowired
    private PlaygroundRepository playgroundRepository;

    @Autowired
    private PlaygroundMemberRepository playgroundMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void deleteAllByIsInsideAndExitTimeBefore() {
        // when
        savePlaygroundMemberWithAndThreeHoursBefore(false);
        savePlaygroundMemberWithAndThreeHoursBefore(false);
        savePlaygroundMemberWithAndThreeHoursBefore(true);

        // given
        playgroundMemberRepository.deleteAllByIsInsideAndExitTimeBefore(false, LocalDateTime.now());
        long playgroundMemberCount = playgroundMemberRepository.count();

        // then
        assertThat(playgroundMemberCount).isOne();
    }

    private void savePlaygroundMemberWithAndThreeHoursBefore(boolean isInside) {
        Playground playground = playgroundRepository.save(new Playground(new Location(1, 1)));
        Member member = memberRepository.save(new Member("이름", "태그", "url"));
        playgroundMemberRepository.save(
                new PlaygroundMember(
                        playground,
                        member,
                        "message",
                        isInside,
                        LocalDateTime.now().minusHours(4),
                        LocalDateTime.now().minusHours(3)
                )
        );
    }

    @DisplayName("ExitTime이 null일 때, 삭제되지 않는다.")
    @Test
    void notDeleteWhenExistTimeIsNull() {
        // given
        Playground playground = playgroundRepository.save(new Playground(new Location(1, 1)));
        Member member = memberRepository.save(new Member("이름", "태그", "iamgeUrl"));
        playgroundMemberRepository.save(
                new PlaygroundMember(
                        playground,
                        member,
                        "",
                        false,
                        LocalDateTime.now(),
                        null
                )
        );
        // when
        playgroundMemberRepository.deleteAllByIsInsideAndExitTimeBefore(false, LocalDateTime.now().minusHours(2));

        // then
        assertThat(playgroundMemberRepository.findAll()).hasSize(1);
    }
}
