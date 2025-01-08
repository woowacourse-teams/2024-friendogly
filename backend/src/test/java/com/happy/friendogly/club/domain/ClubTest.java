package com.happy.friendogly.club.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClubTest {

    private Member member = new Member("member", "1dfhf993", "imageUrl");
    private Pet pet = new Pet(member, "뚱땡이", "설명", LocalDate.now().minusDays(1L), SizeType.SMALL, Gender.FEMALE, "imageUrl");

    @DisplayName("인원이 가득찬 모임 상태를 OPEN으로 변경하면 예외가 발생한다.")
    @Test
    void update_FullToOpen_Fail() {
        Club club = Club.create(
                "title",
                "content",
                "서울특별시",
                "송파구",
                "신천동",
                1,  // 정원 1명, 방장이 인원에 포함되므로, 모임 생성과 동시에 정원이 가득 참
                member,
                Arrays.stream(Gender.values()).collect(Collectors.toSet()),
                Arrays.stream(SizeType.values()).collect(Collectors.toSet()),
                "imageUrl",
                List.of(pet)
        );

        assertThat(club.getStatus()).isEqualTo(Status.FULL);
        assertThatThrownBy(() -> club.update("title", "content", Status.OPEN.name()))
                .isExactlyInstanceOf(FriendoglyException.class)
                .hasMessage("인원이 가득찬 모임은 다시 OPEN 상태로 변경할 수 없습니다.");
    }
}
