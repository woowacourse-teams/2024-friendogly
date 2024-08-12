package com.happy.friendogly.chat.service;

import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.request.InviteToChatRoomRequest;
import com.happy.friendogly.chat.dto.response.ChatRoomDetail;
import com.happy.friendogly.chat.dto.response.FindChatRoomMembersInfoResponse;
import com.happy.friendogly.chat.dto.response.FindMyChatRoomResponse;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.repository.ClubRepository;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChatRoomQueryService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;

    public ChatRoomQueryService(
            ChatRoomRepository chatRoomRepository,
            MemberRepository memberRepository,
            ClubRepository clubRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
        this.clubRepository = clubRepository;
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

    private void validateParticipation(ChatRoom chatRoom, Member member) {
        if (!chatRoom.containsMember(member)) {
            throw new FriendoglyException("채팅방에 참여해 있지 않습니다.");
        }
    }
}
