package com.happy.friendogly.chatroom.service;

import com.happy.friendogly.chatmessage.domain.ChatMessage;
import com.happy.friendogly.chatmessage.repository.ChatMessageRepository;
import com.happy.friendogly.chatroom.domain.ChatRoom;
import com.happy.friendogly.chatroom.dto.request.InviteToChatRoomRequest;
import com.happy.friendogly.chatroom.dto.response.ChatRoomDetail;
import com.happy.friendogly.chatroom.dto.response.ChatRoomDetailV2;
import com.happy.friendogly.chatroom.dto.response.FindChatRoomMembersInfoResponse;
import com.happy.friendogly.chatroom.dto.response.FindClubDetailsResponse;
import com.happy.friendogly.chatroom.dto.response.FindMyChatRoomResponse;
import com.happy.friendogly.chatroom.dto.response.FindMyChatRoomResponseV2;
import com.happy.friendogly.chatroom.repository.ChatRoomRepository;
import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.repository.ClubRepository;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChatRoomQueryService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoomQueryService(
            ChatRoomRepository chatRoomRepository,
            MemberRepository memberRepository,
            ClubRepository clubRepository,
            ChatMessageRepository chatMessageRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
        this.clubRepository = clubRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public FindMyChatRoomResponse findMine(Long memberId) {
        Member member = memberRepository.getById(memberId);
        List<ChatRoomDetail> chatRoomDetails = chatRoomRepository.findMine(member.getId()).stream()
                .map(chatRoom -> clubRepository.findByChatRoomId(chatRoom.getId())
                        .map(club -> new ChatRoomDetail(chatRoom, club.getTitle().getValue(), club.getImageUrl()))
                        .orElse(new ChatRoomDetail(chatRoom, "", ""))
                )
                .toList();
        return new FindMyChatRoomResponse(member.getId(), chatRoomDetails);
    }

    public FindMyChatRoomResponseV2 findMineV2(Long myMemberId) {
        Member member = memberRepository.getById(myMemberId);
        List<ChatRoom> chatRooms = chatRoomRepository.findMine(myMemberId);
        List<ChatRoomDetailV2> chatRoomDetails = chatRooms.stream()
                .map(chatRoom -> toChatRoomDetail(chatRoom, myMemberId))
                .toList();
        return new FindMyChatRoomResponseV2(member.getId(), chatRoomDetails);
    }

    private ChatRoomDetailV2 toChatRoomDetail(ChatRoom chatRoom, Long myMemberId) {
        Optional<ChatMessage> recentMessage = chatMessageRepository.findRecentByChatRoomId(chatRoom.getId());
        String content = recentMessage.map(ChatMessage::getContent).orElse(null);
        LocalDateTime createdAt = recentMessage.map(ChatMessage::getCreatedAt).orElse(null);

        if (chatRoom.isGroupChat()) {
            Club club = clubRepository.findByChatRoomId(chatRoom.getId())
                    .orElseThrow(() -> new FriendoglyException("채팅방에 해당하는 모임이 존재하지 않습니다."));
            return new ChatRoomDetailV2(chatRoom, club.getTitle().getValue(), club.getImageUrl(), content, createdAt);
        }

        if (chatRoom.isPrivateChat()) {
            Optional<Member> otherMember = chatRoom.findMembers().stream()
                    .filter(member -> !myMemberId.equals(member.getId()))
                    .findAny();
            String title = otherMember.map(member -> member.getName().getValue())
                    .orElse(null);
            String imageUrl = otherMember.map(Member::getImageUrl)
                    .orElse(null);
            return new ChatRoomDetailV2(chatRoom, title, imageUrl, content, createdAt);
        }

        throw new FriendoglyException("올바르지 않은 유형의 채팅방입니다.");
    }

    public List<FindChatRoomMembersInfoResponse> findMemberInfo(Long memberId, Long chatRoomId) {
        Member member = memberRepository.getById(memberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        validateParticipation(chatRoom, member);
        Club club = clubRepository.getByChatRoomId(chatRoomId);

        List<Member> members = chatRoom.findMembers();
        return members.stream()
                .map(m -> new FindChatRoomMembersInfoResponse(club.isOwner(m), m))
                .toList();
    }

    public void validateInvitation(Long senderMemberId, InviteToChatRoomRequest request) {
        ChatRoom chatRoom = chatRoomRepository.getById(request.chatRoomId());
        Member sender = memberRepository.getById(senderMemberId);
        validateParticipation(chatRoom, sender);
    }

    public FindClubDetailsResponse findClubDetails(Long memberId, Long chatRoomId) {
        Member member = memberRepository.getById(memberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        validateParticipation(chatRoom, member);
        Club club = clubRepository.getByChatRoomId(chatRoomId);
        return new FindClubDetailsResponse(memberId, club);
    }

    private void validateParticipation(ChatRoom chatRoom, Member member) {
        if (!chatRoom.containsMember(member)) {
            throw new FriendoglyException("채팅방에 참여해 있지 않습니다.");
        }
    }
}
