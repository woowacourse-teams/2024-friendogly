package com.happy.friendogly.notification.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.notification.domain.DeviceToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeviceTokenRepositoryTest {

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @DisplayName("채팅방 ID로부터 device token을 가져올 수 있다.")
    @Test
    void findAllByChatRoomId() {
        // given
        Member member1 = memberRepository.save(new Member("name1", "tag1", "https://test.com/test.jpg"));
        Member member2 = memberRepository.save(new Member("name2", "tag2", "https://test.com/test.jpg"));
        Member member3 = memberRepository.save(new Member("name3", "tag3", "https://test.com/test.jpg"));

        deviceTokenRepository.save(new DeviceToken(member1, "token1"));
        deviceTokenRepository.save(new DeviceToken(member2, "token2"));
        deviceTokenRepository.save(new DeviceToken(member3, "token3"));

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.createGroup(5));
        chatRoom.addMember(member1);
        chatRoom.addMember(member2);

        // when - then
        assertThat(deviceTokenRepository.findAllByChatRoomId(chatRoom.getId())).containsExactly("token1", "token2");
    }
}
