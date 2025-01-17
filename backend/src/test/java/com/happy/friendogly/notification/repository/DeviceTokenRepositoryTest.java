package com.happy.friendogly.notification.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.happy.friendogly.chatroom.domain.ChatRoom;
import com.happy.friendogly.chatroom.repository.ChatRoomRepository;
import com.happy.friendogly.deveicetoken.domain.DeviceToken;
import com.happy.friendogly.deveicetoken.repository.DeviceTokenRepository;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
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

    @DisplayName("채팅방 ID로부터 자기 자신을 제외한 device token들을 가져올 수 있다.")
    @Test
    void findAllByChatRoomId() {
        // given
        Member me = memberRepository.save(new Member("name1", "tag1", "https://test.com/test.jpg"));
        Member other1 = memberRepository.save(new Member("name2", "tag2", "https://test.com/test.jpg"));
        Member other2 = memberRepository.save(new Member("name3", "tag3", "https://test.com/test.jpg"));
        Member notInChatRoom = memberRepository.save(new Member("name4", "tag4", "https://test.com/test.jpg"));

        deviceTokenRepository.save(new DeviceToken(me, "token1"));
        deviceTokenRepository.save(new DeviceToken(other1, "token2"));
        deviceTokenRepository.save(new DeviceToken(other2, "token3"));
        deviceTokenRepository.save(new DeviceToken(notInChatRoom, "token4"));

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.createGroup(5));
        chatRoom.addMember(me);
        chatRoom.addMember(other1);
        chatRoom.addMember(other2);

        // when - then
        assertThat(deviceTokenRepository.findAllByChatRoomIdWithoutMine(chatRoom.getId(), me.getId()))
                .containsExactly("token2", "token3");
    }
}
