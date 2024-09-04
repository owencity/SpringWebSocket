package jjon.pop.repositories;

import jjon.pop.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByChatroomId(Long chatroomId);
    Boolean existsByChatroomIdAndCreatedAtAfter(Long chatroomId, LocalDateTime createdAt);
}
