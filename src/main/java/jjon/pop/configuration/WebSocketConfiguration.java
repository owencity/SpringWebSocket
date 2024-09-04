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
		
		
		// final 필드 -> RequiredArgsConstructor 사용
		// 초기화 위해 생성자 필요 
		final WebSocketChatHandler webSocketChatHandler;
		
	//	public WebSocketConfiguration (WebSocketHandler webSocketHandler) {
	//		this.webSocketChatHandler = webSocketChatHandler;
	//	} Required 어노테이션 사용으로 코드 삭제
		
		@Override
		public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
			
			// WebSocket 핸들러를 특정 엔드포인트에 등록
			registry.addHandler(webSocketChatHandler, "/ws/chats"); // 해당 path 에 핸들러 연ㅈ결
		}
	}
