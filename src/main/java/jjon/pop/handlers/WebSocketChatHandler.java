
package jjon.pop.handlers;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j // Lombok을 사용하여 로그 출력을 간편하게 처리
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {
	
	
	// WebSocket 세션을 저장하는 맵 (세션 ID를 키로 사용)
	// ConcurrrentHashMap을 사용하여 동시성 문제해결
    final Map<String, WebSocketSession> webSocketSessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 클라이언트 WebSocket 연결 성공적으로 맺었을 때
    	log.info("{} connected", session.getId());
        this.webSocketSessionMap.put(session.getId(), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 클라이언트로부터 메시지를 받았을 떄
    	log.info("{} sent {}", session.getId(), message.getPayload());
        
    	// 모든 연결된 세션에 메시지 브로드캐스팅
        this.webSocketSessionMap.values().forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 클라이언트 WebSocket 연결 종료되었을 때 호출
    	log.info("{} disconnected", session.getId()); // 연결 헤제된 세션ID 로깅 
        this.webSocketSessionMap.remove(session.getId()); // 세션 정보를 맵에서 제거
    }
}
