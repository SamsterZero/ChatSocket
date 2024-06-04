package in.vvm.chat_socket.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MessageDTO {

    private Long id;

    @Size(max = 255)
    private String content;

    private Long sender;

    private Long chatRoom;

}
