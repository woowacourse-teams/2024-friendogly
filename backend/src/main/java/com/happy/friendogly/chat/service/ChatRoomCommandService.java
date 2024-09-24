package com.happy.friendogly.chat.service;

import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.request.SaveChatRoomRequest;
import com.happy.friendogly.chat.dto.response.EnterChatRoomResponse;
import com.happy.friendogly.chat.dto.response.SaveChatRoomResponse;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.repository.ClubRepository;
import com.happy.friendogly.exception.FriendoglyException;
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
    private final ClubRepository clubRepository;

    private final ChatCommandService chatCommandService;

    public ChatRoomCommandService(
            ChatRoomRepository chatRoomRepository,
            MemberRepository memberRepository,
            ClubRepository clubRepository,
            ChatCommandService chatCommandService
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
        this.clubRepository = clubRepository;
        this.chatCommandService = chatCommandService;
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

        chatCommandService.sendLeave(memberId, chatRoomId);
    }

    public EnterChatRoomResponse enter(Long memberId, Long chatRoomId) {
        Member member = memberRepository.getById(memberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);

        if (chatRoom.isPrivateChat()) {
            validateInvitation(chatRoom, member);
        }

        if (chatRoom.isGroupChat()) {
            validateClubParticipation(chatRoom, member);
        }

        chatCommandService.sendEnter(memberId, chatRoomId);
        return new EnterChatRoomResponse(chatRoomId);
    }

    private void validateInvitation(ChatRoom chatRoom, Member member) {
        if (!chatRoom.containsMember(member)) {
            throw new FriendoglyException("채팅에 참여한 상태가 아닙니다.");
        }
    }

    private void validateClubParticipation(ChatRoom chatRoom, Member member) {
        Club club = clubRepository.getByChatRoomId(chatRoom.getId());

        if (!club.isAlreadyJoined(member)) {
            throw new FriendoglyException("모임에 참여해야만 채팅에 참여할 수 있습니다.");
        }
    }
}
