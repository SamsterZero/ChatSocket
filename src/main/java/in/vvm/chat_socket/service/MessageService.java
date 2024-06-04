package in.vvm.chat_socket.service;

import in.vvm.chat_socket.domain.ChatRoom;
import in.vvm.chat_socket.domain.Message;
import in.vvm.chat_socket.domain.User;
import in.vvm.chat_socket.model.MessageDTO;
import in.vvm.chat_socket.repos.ChatRoomRepository;
import in.vvm.chat_socket.repos.MessageRepository;
import in.vvm.chat_socket.repos.UserRepository;
import in.vvm.chat_socket.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public MessageService(final MessageRepository messageRepository,
            final UserRepository userRepository, final ChatRoomRepository chatRoomRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public List<MessageDTO> findAll() {
        final List<Message> messages = messageRepository.findAll(Sort.by("id"));
        return messages.stream()
                .map(message -> mapToDTO(message, new MessageDTO()))
                .toList();
    }

    public MessageDTO get(final Long id) {
        return messageRepository.findById(id)
                .map(message -> mapToDTO(message, new MessageDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MessageDTO messageDTO) {
        final Message message = new Message();
        mapToEntity(messageDTO, message);
        return messageRepository.save(message).getId();
    }

    public void update(final Long id, final MessageDTO messageDTO) {
        final Message message = messageRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(messageDTO, message);
        messageRepository.save(message);
    }

    public void delete(final Long id) {
        messageRepository.deleteById(id);
    }

    private MessageDTO mapToDTO(final Message message, final MessageDTO messageDTO) {
        messageDTO.setId(message.getId());
        messageDTO.setContent(message.getContent());
        messageDTO.setSender(message.getSender() == null ? null : message.getSender().getId());
        messageDTO.setChatRoom(message.getChatRoom() == null ? null : message.getChatRoom().getId());
        return messageDTO;
    }

    private Message mapToEntity(final MessageDTO messageDTO, final Message message) {
        message.setContent(messageDTO.getContent());
        final User sender = messageDTO.getSender() == null ? null : userRepository.findById(messageDTO.getSender())
                .orElseThrow(() -> new NotFoundException("sender not found"));
        message.setSender(sender);
        final ChatRoom chatRoom = messageDTO.getChatRoom() == null ? null : chatRoomRepository.findById(messageDTO.getChatRoom())
                .orElseThrow(() -> new NotFoundException("chatRoom not found"));
        message.setChatRoom(chatRoom);
        return message;
    }

}
