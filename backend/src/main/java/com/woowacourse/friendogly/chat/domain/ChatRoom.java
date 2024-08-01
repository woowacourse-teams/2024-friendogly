package com.woowacourse.friendogly.chat.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    public void addMember(Member member) {
        if (containsMember(member)) {
            throw new FriendoglyException("이미 참여한 채팅방입니다.");
        }
        chatRoomMembers.add(new ChatRoomMember(this, member));
    }

    private boolean containsMember(Member member) {
        return chatRoomMembers.stream()
                .anyMatch(chatRoomMember -> chatRoomMember.hasMember(member));
    }

    public void removeMember(Member member) {
        ChatRoomMember chatRoomMember = chatRoomMembers.stream()
                .filter(row -> row.hasMember(member))
                .findAny()
                .orElseThrow(() -> new FriendoglyException("자신이 참여한 채팅방에서만 나갈 수 있습니다."));

        chatRoomMembers.remove(chatRoomMember);
        chatRoomMember.removeChatRoom();
    }

    // TODO: 미사용 메서드
    public int countMembers() {
        return chatRoomMembers.size();
    }

    public boolean isEmpty() {
        return chatRoomMembers.isEmpty();
    }
}
