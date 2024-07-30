package com.woowacourse.friendogly.chat.domain;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PrivateChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_member_id")
    private Member otherMember;

    public PrivateChatRoom(Member member, Member otherMember) {
        validate(member, otherMember);
        this.member = member;
        this.otherMember = otherMember;
    }

    private void validate(Member member, Member otherMember) {
        if (member.getId().equals(otherMember.getId())) {
            throw new FriendoglyException("자기 자신을 채팅방에 초대할 수 없습니다.", BAD_REQUEST);
        }
    }

    public void leave(Member member) {
        if (this.member != null && this.member.getId().equals(member.getId())) {
            this.member = null;
            return;
        }
        if (this.otherMember != null && this.otherMember.getId().equals(member.getId())) {
            this.otherMember = null;
            return;
        }
        throw new FriendoglyException("자신이 참여한 채팅방만 나갈 수 있습니다.");
    }

    public boolean isEmpty() {
        return member == null && otherMember == null;
    }
}
