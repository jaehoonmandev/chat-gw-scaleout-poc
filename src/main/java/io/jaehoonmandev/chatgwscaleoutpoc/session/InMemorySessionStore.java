package io.jaehoonmandev.chatgwscaleoutpoc.session;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JVM 메모리 세션 저장
 */
@Component
public class InMemorySessionStore implements ChatSessionStore {

    // JVM 메모리에서 세션 관리
    private final ConcurrentHashMap<String, ChatSession> sessions = new ConcurrentHashMap<>();

    @Override
    public ChatSession getOrCreate(ChatSessionKey key) {
        // get() 후 put()으로 나누면 동시 요청 두 개가 둘 다 "없음"을 보고 세션을 중복 생성할 수 있다.
        // computeIfAbsent는 같은 키에 대해 원자적으로 동작해 그 레이스를 막는다.
        return sessions.computeIfAbsent(key.asChatSessionKey(), k -> new ChatSession(key));
    }

    @Override
    public Optional<ChatSession> find(ChatSessionKey key) {
        // 세션 정보가 없을 수 있으니 NPE 방지를 위해 Optional
        return Optional.ofNullable(sessions.get(key.asChatSessionKey()));
    }

    @Override
    public Collection<ChatSession> findAll() {
        return List.copyOf(sessions.values());
    }

    @Override
    public void remove(ChatSessionKey key) {
        sessions.remove(key.asChatSessionKey());
    }
}
