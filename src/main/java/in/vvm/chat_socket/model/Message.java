package in.vvm.chat_socket.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Message {

    @Size(max = 255)
    private String sender;

    @Size(max = 255)
    private String content;

    private LocalDateTime timestamp;

}
