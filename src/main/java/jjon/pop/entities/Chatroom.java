package jjon.pop.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Chatroom {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    @Id
    Long id;

    String title;
    @OneToMany(mappedBy = "chatroom")
    Set<MemberChatroomMapping> memberChatroomMappingSet;
    // Set 사용하여 중복불가 , 동일한 객체 저장 방지
    // 특정 상황에서 중복이 논리적으로 허용되지 않는 경우(예: 동일한 회원이 동일한 채팅방에 여러 번 매핑될 수 없을 때)
    LocalDateTime createAt;

    @Transient
    Boolean hasNewMessage;

    // 새로운 메시지 여부 확인
    public void setHasNewMessage(Boolean hasNewMessage) {
        this.hasNewMessage = hasNewMessage;
    }
    // 채팅방에 새로운 멤버 추가, 매핑 정보 반환
    public MemberChatroomMapping addMember(Member member) {
        if(this.getMemberChatroomMappingSet() == null) {
            // null 이라는 것은 아직 set 이 초기화 되지않았음을 의미, 즉 이 채팅방에 아직 어떤 객체도 추가되지않았다는것을 의미
            this.memberChatroomMappingSet = new HashSet<>();
            // null 이면 HashSet객체를 생성하여 할당 , 이렇게되면 MappinSet 이 null이 아니게되어 이후에 이 set 요소에 추가하거나 접근가능하다.
            // null인 상태에서 set에 요소를 추가하려고하면 nullException이 발생할수 있어 이를 미리 방지하는 역할
        }
        //mapping 객체 생성하여 회원과 채팅방의 매핑정보를 설정
        MemberChatroomMapping memberChatroomMapping = MemberChatroomMapping.builder()
                .member(member)
                .chatroom(this)
                .build();

        this.memberChatroomMappingSet.add(memberChatroomMapping);
        return memberChatroomMapping;
        // mappingset에 담아 mapping 리턴
    }

}
