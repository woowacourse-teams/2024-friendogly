package com.happy.friendogly.chat.service;

import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.request.SaveChatRoomRequest;
import com.happy.friendogly.chat.dto.response.SaveChatRoomResponse;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
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
}
