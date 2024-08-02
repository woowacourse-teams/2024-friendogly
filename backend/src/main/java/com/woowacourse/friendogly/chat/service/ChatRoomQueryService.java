package com.woowacourse.friendogly.chat.service;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.chat.dto.response.FindChatRoomMembersInfoResponse;
import com.woowacourse.friendogly.chat.dto.response.FindMyChatRoomResponse;
import com.woowacourse.friendogly.chat.repository.ChatRoomRepository;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChatRoomQueryService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public ChatRoomQueryService(
            ChatRoomRepository chatRoomRepository,
            MemberRepository memberRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
    }

    public List<FindMyChatRoomResponse> findMine(Long memberId) {
        return chatRoomRepository.findMine(memberId).stream()
                .map(FindMyChatRoomResponse::new)
                .toList();
    }

    public List<FindChatRoomMembersInfoResponse> findMemberInfo(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        List<Member> members = chatRoom.findMembers();
        return members.stream()
                .map(FindChatRoomMembersInfoResponse::new)
                .toList();
    }

    public void validateParticipation(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        Member member = memberRepository.getById(memberId);
        if (!chatRoom.containsMember(member)) {
            throw new FriendoglyException("채팅방에 참여해 있지 않습니다.");
        }
    }
}
