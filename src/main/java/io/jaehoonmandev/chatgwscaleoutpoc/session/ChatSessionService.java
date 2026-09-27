package io.jaehoonmandev.chatgwscaleoutpoc.session;

import io.jaehoonmandev.chatgwscaleoutpoc.webhook.UserMessageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 채팅 세션 비즈니스 로직 서비스
 */
@Service
public class ChatSessionService {
    private final ChatSessionStore chatSessionStore;

    public ChatSessionService(ChatSessionStore chatSessionStore) {
        this.chatSessionStore = chatSessionStore;
    }

    /**
     * 고객 전송 메시지 이벤트 처리
     * @param request 고객 메시지 이벤트 본문
     * @return 조회 || 생성된 세션
     */
    public ChatSession handleMessage(UserMessageRequest request) {
        ChatSessionKey key = new ChatSessionKey(request.getSenderKey(), request.getUserKey());
        ChatSession session = chatSessionStore.getOrCreate(key);
        session.processMessage(request);
        return session;
    }

    /**
     * 특정 채팅 세션 조회
     * @param senderKey 채널키
     * @param userKey   유저키
     * @return          조회된 채팅 세션 || "조회되지 않음" 객체
     */
    public Optional<ChatSession> findSession(String senderKey, String userKey) {
        return chatSessionStore.find(new ChatSessionKey(senderKey, userKey));
    }
}
