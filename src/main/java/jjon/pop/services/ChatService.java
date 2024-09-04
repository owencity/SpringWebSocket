package jjon.pop.services;

import jjon.pop.dtos.ChatroomDto;
import jjon.pop.entities.Chatroom;
import jjon.pop.entities.Member;
import jjon.pop.entities.MemberChatroomMapping;
import jjon.pop.entities.Message;
import jjon.pop.repositories.ChatroomRepository;
import jjon.pop.repositories.MemberChatroomMappingRepository;
import jjon.pop.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatroomRepository chatroomRepository;
    private final MemberChatroomMappingRepository memberChatroomMappingRepository;
    private final MessageRepository messageRepository;

    // 사용자가 채팅방에 입장하거나 새 채팅방을 생성할 때 이 메서드가 실행됩니다.
// 이 메서드는 채팅방을 생성하고, 해당 채팅방에 사용자를 추가한 후, 생성된 채팅방 객체를 반환합니다.
// 반환된 채팅방 객체는 저장된 후 사용자에게 보여주기 위해 반환됩니다.
    public Chatroom createChatroom(Member member, String title) {
        Chatroom chatroom = Chatroom.builder()
                .title(title)
                .createAt(LocalDateTime.now())
                .build();
        chatroom = chatroomRepository.save(chatroom);
        // 저장한 chatroomd을  chatroom에 저장
        MemberChatroomMapping memberChatroomMapping = chatroom.addMember(member);
        //
        memberChatroomMapping = memberChatroomMappingRepository.save(memberChatroomMapping);

        return chatroom;
    }

    // 사용자가 chatroom을 입장했을떄 아래 메서드 발생
    // 어떠한 데이터를 반환하기보다는 입장과 입장실패를 위해 boolean 값으로 반환하여 성공실패를 반환
    public Boolean joinChatroom(Member member, Long newChatroomId, Long currentChatroomId) {
        if (currentChatroomId != null) {
            updateLastCheckedAt(member, currentChatroomId);
        }
        // 사용자가 현재 참여중인 채팅방의 ID 가 null이 아닌 경우
        // 해당 채팅방에서 사용자가 마지막으로 확인한 시간을 업데이트 하는 메서드를 호출
        if (memberChatroomMappingRepository.existsByMemberIdAndChatroomId(member.getId(), newChatroomId)) {
            log.info("이미 참여한 채팅방입니다.");
            return false;
        }
        // 매핑된채팅방에서 아이디와 새로운 chatid를 확인하여 이미 참여한채팅방인지 확인
        Chatroom chatroom = chatroomRepository.findById(newChatroomId).get();
        // findById는 Spring Data JPA에서 제공하는 메서드로, Optional<Chatroom>을 반환합니다.
        // Optional은 값이 존재할 수도 있고, 존재하지 않을 수도 있으므로,
        // get() 메서드를 사용하여 실제 Chatroom 객체를 가져옵니다.
        // 그러나 실제 코드에서는 get()을 바로 사용하기보다는, Optional이 비어있는 경우를 처리하는 것이 더 안전합니다.
        MemberChatroomMapping memberChatroomMapping = MemberChatroomMapping.builder()
                .member(member)
                .chatroom(chatroom)
                .build();
        memberChatroomMapping = memberChatroomMappingRepository.save(memberChatroomMapping);
        // 새로운 사용자를 해당 채팅방에 매핑하여 매핑 정보를 저장하고, 입장이 성공했음을 의미하는 true를 반환합니다.
        return true;
    }

    private void updateLastCheckedAt(Member member , Long currentChatroomId) {
        MemberChatroomMapping memberChatroomMapping = memberChatroomMappingRepository.findByMemberIdAndChatroomId(member.getId(), currentChatroomId)
                .get();

                memberChatroomMapping.updateLastCheckedAt();

                memberChatroomMappingRepository.save(memberChatroomMapping);
                // 특정사용자 채팅방 마지막확인시간 저장, 활용하여 새로운 메시지 알림 기능구현가능
    }
    // 트랜잭션적용하여 삭제작업중 문제발생하면 자동으로 롤백
    @Transactional
    public Boolean leaveChatroom(Member member, Long chatroomId) {
        if(!memberChatroomMappingRepository.existsByMemberIdAndChatroomId(member.getId(), chatroomId)) {
            log.info("참여하지 않은 방입니다.");
            return false;
        }
        // exists 인증로직으로 채팅방에 참여하고있는지 검증 후 퇴장

        // 2번 쿼리실행으로 인한 개선 방향성
        memberChatroomMappingRepository.deleteByMemberIdAndChatroomId(member.getId(), chatroomId);
        return true;
        // 하나의쿼리로 개선 , 아무것도 삭제하지않으면 false 하는 구조로 변경 , 조회결과를 바로삭제 하는방법
    }
    public List<Chatroom> getChatroomList(Member member){
        List<MemberChatroomMapping> memberChatroomMappingList = memberChatroomMappingRepository.findAllByMemberId(member.getId());

        return memberChatroomMappingList.stream()
                .map(memberChatroomMapping -> {
                    Chatroom chatroom = memberChatroomMapping.getChatroom();
                    chatroom.setHasNewMessage(messageRepository.existsByChatroomIdAndCreatedAtAfter(chatroom.getId(), memberChatroomMapping.getLastCheckedAt()));

                    return chatroom;
                })
                .toList();
    }
    // 멤버와 채팅방과의 연관관계 없어서 멤버를 통해 채팅방을 가져올수없음 현재
    // 채팅방참여

    public Message saveMessage(Member member, Long chatroomId, String text) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId).get();

        Message message = Message.builder()
                .text(text)
                .member(member)
                .chatroom(chatroom)
                .createdAt(LocalDateTime.now())
                .build();
        return messageRepository.save(message);
    }
   public List<Message> getMessageList(Long chatroomId) {
        return messageRepository.findAllByChatroomId(chatroomId);
   }

    @Transactional(readOnly = true)
    public ChatroomDto getChatroom(Long chatroomId) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId).get();
        return ChatroomDto.from(chatroom);
   }
}
