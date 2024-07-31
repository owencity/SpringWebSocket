package jjon.pop.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import jjon.pop.handlers.WebSocketChatHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
	
	final WebSocketChatHandler webSocketChatHandler;
	
//	public WebSocketConfiguration (WebSocketHandler webSocketHandler) {
//		this.webSocketChatHandler = webSocketChatHandler;
//	} Required 어노테이션 사용으로 코드 삭제
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addHandler(webSocketChatHandler, "/ws/chats");
	}
}
