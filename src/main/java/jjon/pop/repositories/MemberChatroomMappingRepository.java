package jjon.pop.repositories;


import jjon.pop.entities.MemberChatroomMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberChatroomMappingRepository extends JpaRepository <MemberChatroomMapping, Long> {
    Boolean existsByMemberIdAndChatroomId(Long memberId, Long chatroomId);
    void deleteByMemberIdAndChatroomId(Long memberId, long chatroomId);
    List<MemberChatroomMapping> findAllByMemberId(Long memberId);

   Optional<MemberChatroomMapping> findByMemberIdAndChatroomId(Long memberId , Long chatroomId);
}
