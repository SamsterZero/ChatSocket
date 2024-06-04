package in.vvm.chat_socket.repos;

import in.vvm.chat_socket.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
