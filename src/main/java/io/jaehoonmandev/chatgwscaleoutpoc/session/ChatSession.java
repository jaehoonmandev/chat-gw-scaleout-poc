package io.jaehoonmandev.chatgwscaleoutpoc.session;

import io.jaehoonmandev.chatgwscaleoutpoc.webhook.UserMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * 채팅 세션 데이터 객체
 */
public class ChatSession{
    private static final Logger log = LoggerFactory.getLogger(ChatSession.class);

    private ChatSessionKey      sessionKey;     // senderKey:userKey 조합으로 관리 중인 세션 키
    private LocalDateTime       startTime;      // 최초 세션 생성 시간
    private LocalDateTime       lastMessageAt;  // 마지막 메시지 송/수신 시간
    private ChatSessionState    state;          // 채팅 상태(생성, 연결, 연결중, 만료 등)

    // 신규 인스턴스 생성 시 초기 상태 설정 생성자
    public ChatSession(ChatSessionKey sessionKey){
        this.sessionKey = sessionKey;
        this.startTime = LocalDateTime.now();
        this.lastMessageAt = this.startTime;
        this.state = ChatSessionState.CREATED;
    }

    // 메시지 처리
    public void processMessage(UserMessageRequest request){
        log.debug("Received UserMessageRequest : {}", request.getMessage());

        this.lastMessageAt = LocalDateTime.now(); // 호출 시 마지막 메시징 시간 변경
    }

    public ChatSessionKey getSessionKey() { return sessionKey; }
    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
    public ChatSessionState getState() {
        return state;
    }
}
