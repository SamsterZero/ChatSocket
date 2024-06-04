package in.vvm.chat_socket.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {
     @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Called when a new WebSocket connection is established
        super.afterConnectionEstablished(session);
        System.out.println("WebSocket connection opened: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming text messages
        String receivedMessage = message.getPayload();
        System.out.println("Received message: " + receivedMessage);

        // You can send a response back to the client if needed
        String responseMessage = "Hello from the server!";
        session.sendMessage(new TextMessage(responseMessage));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Called when a WebSocket connection is closed
        super.afterConnectionClosed(session, status);
        System.out.println("WebSocket connection closed: " + session.getId());
    }
}
