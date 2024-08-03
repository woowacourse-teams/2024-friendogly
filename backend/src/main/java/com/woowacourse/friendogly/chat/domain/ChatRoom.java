package com.woowacourse.friendogly.chat.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
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

    @Embedded
    private MemberCapacity memberCapacity;

    public ChatRoom(int capacity) {
        this.memberCapacity = new MemberCapacity(capacity);
    }

    public static ChatRoom createPrivate(Member initialMember) {
        ChatRoom chatRoom = new ChatRoom(2);
        chatRoom.addMember(initialMember);
        return chatRoom;
    }

    public void addMember(Member member) {
        if (containsMember(member)) {
            throw new FriendoglyException("이미 참여한 채팅방입니다.");
        }
        if (isFull()) {
            throw new FriendoglyException("정원을 초과한 채팅방입니다.");
        }
        chatRoomMembers.add(new ChatRoomMember(this, member));
    }

    private boolean isFull() {
        return chatRoomMembers.size() >= memberCapacity.getValue();
    }

    public void removeMember(Member member) {
        ChatRoomMember chatRoomMember = chatRoomMembers.stream()
                .filter(row -> row.hasMember(member))
                .findAny()
                .orElseThrow(() -> new FriendoglyException("자신이 참여한 채팅방만 나갈 수 있습니다."));

        chatRoomMembers.remove(chatRoomMember);
    }

    public boolean containsMember(Member member) {
        return chatRoomMembers.stream()
                .anyMatch(chatRoomMember -> chatRoomMember.hasMember(member));
    }

    public int countMembers() {
        return chatRoomMembers.size();
    }

    public boolean isEmpty() {
        return chatRoomMembers.isEmpty();
    }

    public List<String> findMemberNames() {
        return chatRoomMembers.stream()
                .map(ChatRoomMember::findMemberName)
                .toList();
    }

    public List<Member> findMembers() {
        return chatRoomMembers.stream()
                .map(ChatRoomMember::getMember)
                .toList();
    }
}
