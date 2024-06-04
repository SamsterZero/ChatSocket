package in.vvm.chat_socket.service;

import in.vvm.chat_socket.domain.ChatRoom;
import in.vvm.chat_socket.domain.Message;
import in.vvm.chat_socket.domain.User;
import in.vvm.chat_socket.model.UserDTO;
import in.vvm.chat_socket.repos.ChatRoomRepository;
import in.vvm.chat_socket.repos.MessageRepository;
import in.vvm.chat_socket.repos.UserRepository;
import in.vvm.chat_socket.util.NotFoundException;
import in.vvm.chat_socket.util.ReferencedWarning;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public UserService(final UserRepository userRepository,
            final ChatRoomRepository chatRoomRepository,
            final MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setChatRooms(user.getChatRooms().stream()
                .map(chatRoom -> chatRoom.getId())
                .toList());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        final List<ChatRoom> chatRooms = chatRoomRepository.findAllById(
                userDTO.getChatRooms() == null ? Collections.emptyList() : userDTO.getChatRooms());
        if (chatRooms.size() != (userDTO.getChatRooms() == null ? 0 : userDTO.getChatRooms().size())) {
            throw new NotFoundException("one of chatRooms not found");
        }
        user.setChatRooms(new HashSet<>(chatRooms));
        return user;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Message senderMessage = messageRepository.findFirstBySender(user);
        if (senderMessage != null) {
            referencedWarning.setKey("user.message.sender.referenced");
            referencedWarning.addParam(senderMessage.getId());
            return referencedWarning;
        }
        return null;
    }

}
