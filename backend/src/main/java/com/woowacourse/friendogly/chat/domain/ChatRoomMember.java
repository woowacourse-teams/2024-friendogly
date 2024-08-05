package com.woowacourse.friendogly.chat.domain;

import com.woowacourse.friendogly.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private LocalDateTime createdAt;

    public ChatRoomMember(ChatRoom chatRoom, Member member) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.createdAt = LocalDateTime.now();
    }

    public boolean hasMember(Member member) {
        if (member == null) {
            return false;
        }
        return this.member.getId().equals(member.getId());
    }
}
