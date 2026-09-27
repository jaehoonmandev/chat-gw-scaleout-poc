package io.jaehoonmandev.chatgwscaleoutpoc.session;

import java.util.Collection;
import java.util.Optional;

/**
 * 채팅 세션 저장 인터페이스.
 * 구현체를 InMemorySessionStore → RedisSessionStore로 교체할 지점.
 */
public interface ChatSessionStore {
    ChatSession getOrCreate(ChatSessionKey key);    // 세션 정보가 있다면 get 없다면 create
    Optional<ChatSession> find(ChatSessionKey key); // 특정 세션 조회
    Collection<ChatSession> findAll();              // 전체 세션 조회
    void remove(ChatSessionKey key);                // 세션 삭제(timeout 삭제 등)
}
