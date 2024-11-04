package com.happy.friendogly.chatroom.service;

import com.happy.friendogly.chatroom.domain.ChatRoom;
import com.happy.friendogly.chatroom.dto.request.SaveChatRoomRequest;
import com.happy.friendogly.chatroom.dto.response.SaveChatRoomResponse;
import com.happy.friendogly.chatroom.repository.ChatRoomRepository;
import com.happy.friendogly.chatsocket.service.ChatSocketCommandService;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatRoomCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    private final ChatSocketCommandService chatSocketCommandService;

    public ChatRoomCommandService(
            ChatRoomRepository chatRoomRepository,
            MemberRepository memberRepository,
            ChatSocketCommandService chatSocketCommandService
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
        this.chatSocketCommandService = chatSocketCommandService;
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
        chatRoom.removeMember(member);

        chatSocketCommandService.sendLeave(memberId, chatRoomId);
    }
}
