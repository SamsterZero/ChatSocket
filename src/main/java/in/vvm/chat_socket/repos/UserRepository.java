package in.vvm.chat_socket.repos;

import in.vvm.chat_socket.domain.ChatRoom;
import in.vvm.chat_socket.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByChatRooms(ChatRoom chatRoom);

    List<User> findAllByChatRooms(ChatRoom chatRoom);

}
