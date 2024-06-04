package in.vvm.chat_socket.repos;

import in.vvm.chat_socket.domain.ChatRoom;
import in.vvm.chat_socket.domain.Message;
import in.vvm.chat_socket.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {

    Message findFirstBySender(User user);

    Message findFirstByChatRoom(ChatRoom chatRoom);

}
