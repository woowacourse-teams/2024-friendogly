package com.woowacourse.friendogly.chat.service;

import static com.woowacourse.friendogly.pet.domain.Gender.MALE;
import static com.woowacourse.friendogly.pet.domain.SizeType.SMALL;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.support.ServiceTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ChatRoomQueryServiceTest extends ServiceTest {

    @Autowired
    private ChatRoomQueryService chatRoomQueryService;

    @DisplayName("memberId와 chatRoomId로 모임의 구성원 여부를 판별할 수 있다.")
    @Test
    void validate() {
        // given
        Member validMember = memberRepository.save(new Member("name", "tag", "a@a.com", "https://a.com"));
        Member invalidMember = memberRepository.save(new Member("nn", "tt", "b@b.com", "https://b.com"));
        Pet pet = petRepository.save(
                new Pet(validMember, "pet", "desc", LocalDate.now().minusYears(1), SMALL, MALE, "https://a.com"));
        Club club = clubRepository.save(
                Club.create("t", "c", "a", 5, validMember, Set.of(MALE), Set.of(SMALL), "https://a.com", List.of(pet)));
        ChatRoom chatRoom = club.getChatRoom();

        // when - then
        assertAll(
                () -> assertThatCode(() -> chatRoomQueryService.validate(validMember.getId(), chatRoom.getId()))
                        .doesNotThrowAnyException(),
                () -> assertThatThrownBy(() -> chatRoomQueryService.validate(invalidMember.getId(), chatRoom.getId()))
                        .isInstanceOf(FriendoglyException.class)
                        .hasMessage("모임의 구성원이 아닙니다.")
        );
    }
}
