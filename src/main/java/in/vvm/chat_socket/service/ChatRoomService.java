package in.vvm.chat_socket.service;

import in.vvm.chat_socket.domain.ChatRoom;
import in.vvm.chat_socket.domain.Message;
import in.vvm.chat_socket.model.ChatRoomDTO;
import in.vvm.chat_socket.repos.ChatRoomRepository;
import in.vvm.chat_socket.repos.MessageRepository;
import in.vvm.chat_socket.repos.UserRepository;
import in.vvm.chat_socket.util.NotFoundException;
import in.vvm.chat_socket.util.ReferencedWarning;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public ChatRoomService(final ChatRoomRepository chatRoomRepository,
            final UserRepository userRepository, final MessageRepository messageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public List<ChatRoomDTO> findAll() {
        final List<ChatRoom> chatRooms = chatRoomRepository.findAll(Sort.by("id"));
        return chatRooms.stream()
                .map(chatRoom -> mapToDTO(chatRoom, new ChatRoomDTO()))
                .toList();
    }

    public ChatRoomDTO get(final Long id) {
        return chatRoomRepository.findById(id)
                .map(chatRoom -> mapToDTO(chatRoom, new ChatRoomDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ChatRoomDTO chatRoomDTO) {
        final ChatRoom chatRoom = new ChatRoom();
        mapToEntity(chatRoomDTO, chatRoom);
        return chatRoomRepository.save(chatRoom).getId();
    }

    public void update(final Long id, final ChatRoomDTO chatRoomDTO) {
        final ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(chatRoomDTO, chatRoom);
        chatRoomRepository.save(chatRoom);
    }

    public void delete(final Long id) {
        final ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        userRepository.findAllByChatRooms(chatRoom)
                .forEach(user -> user.getChatRooms().remove(chatRoom));
        chatRoomRepository.delete(chatRoom);
    }

    private ChatRoomDTO mapToDTO(final ChatRoom chatRoom, final ChatRoomDTO chatRoomDTO) {
        chatRoomDTO.setId(chatRoom.getId());
        chatRoomDTO.setName(chatRoom.getName());
        chatRoomDTO.setDescription(chatRoom.getDescription());
        return chatRoomDTO;
    }

    private ChatRoom mapToEntity(final ChatRoomDTO chatRoomDTO, final ChatRoom chatRoom) {
        chatRoom.setName(chatRoomDTO.getName());
        chatRoom.setDescription(chatRoomDTO.getDescription());
        return chatRoom;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Message chatRoomMessage = messageRepository.findFirstByChatRoom(chatRoom);
        if (chatRoomMessage != null) {
            referencedWarning.setKey("chatRoom.message.chatRoom.referenced");
            referencedWarning.addParam(chatRoomMessage.getId());
            return referencedWarning;
        }
        return null;
    }

}
