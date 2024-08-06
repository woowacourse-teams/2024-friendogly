package com.woowacourse.friendogly.chat.service;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.chat.dto.request.SaveChatRoomRequest;
import com.woowacourse.friendogly.chat.dto.response.SaveChatRoomResponse;
import com.woowacourse.friendogly.chat.repository.ChatRoomRepository;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatRoomCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public ChatRoomCommandService(
            ChatRoomRepository chatRoomRepository,
            MemberRepository memberRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
    }

    public SaveChatRoomResponse savePrivate(Long memberId, SaveChatRoomRequest request) {
        Member member = memberRepository.getById(memberId);
        Member otherMember = memberRepository.getById(request.otherMemberId());

        ChatRoom chatRoom = findExistingChatRoom(memberId, otherMember)
                .orElse(chatRoomRepository.save(ChatRoom.createPrivate(member, otherMember)));

        return new SaveChatRoomResponse(chatRoom.getId());
    }

    private Optional<ChatRoom> findExistingChatRoom(Long memberId, Member otherMember) {
        List<ChatRoom> chatRooms = chatRoomRepository.findMine(memberId);
        return chatRooms.stream()
                .filter(chatRoom -> chatRoom.containsMember(otherMember))
                .findAny();
    }

    public void leave(Long memberId, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        Member member = memberRepository.getById(memberId);
        validateParticipation(chatRoom, member);
        chatRoom.removeMember(member);
    }

    private void validateParticipation(ChatRoom chatRoom, Member member) {
        // TODO: 웹 소켓에서는 예외 처리 로직을 다르게 가져가야 함. 400 에러가 터지더라도 정상적으로 채팅방에 들어가진다.
        if (!chatRoom.containsMember(member)) {
            throw new FriendoglyException("채팅에 참여한 상태가 아닙니다.");
        }
    }

    public void enter(Long memberId, Long chatRoomId) {
        Member member = memberRepository.getById(memberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        if (chatRoom.isGroupChat()) {
            chatRoom.addMember(member);
        }
    }
}
