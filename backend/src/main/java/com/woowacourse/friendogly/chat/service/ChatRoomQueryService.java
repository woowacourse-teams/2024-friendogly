package com.woowacourse.friendogly.chat.service;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.chat.dto.request.InviteToChatRoomRequest;
import com.woowacourse.friendogly.chat.dto.response.ChatRoomDetail;
import com.woowacourse.friendogly.chat.dto.response.FindChatRoomMembersInfoResponse;
import com.woowacourse.friendogly.chat.dto.response.FindMyChatRoomResponse;
import com.woowacourse.friendogly.chat.repository.ChatRoomRepository;
import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.repository.ClubRepository;
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
        List<ChatRoomDetail> chatRoomDetails = chatRoomRepository.findMine(memberId).stream()
                .map(chatRoom -> {
                    Club club = clubRepository.getByChatRoom(chatRoom);
                    return new ChatRoomDetail(chatRoom, club);
                })
                .toList();
        return new FindMyChatRoomResponse(memberId, chatRoomDetails);
    }

    public List<FindChatRoomMembersInfoResponse> findMemberInfo(Long memberId, Long chatRoomId) {
        Member member = memberRepository.getById(memberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        validateParticipation(chatRoom, member);
        Club club = clubRepository.getByChatRoom(chatRoom);

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
