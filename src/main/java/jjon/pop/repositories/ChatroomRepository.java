package jjon.pop.repositories;

import jjon.pop.entities.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository <Chatroom, Long> {

}
