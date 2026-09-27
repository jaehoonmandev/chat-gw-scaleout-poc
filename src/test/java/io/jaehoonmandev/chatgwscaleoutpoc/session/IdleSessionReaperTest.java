package io.jaehoonmandev.chatgwscaleoutpoc.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IdleSessionReaperTest {

    private InMemorySessionStore store;

    @BeforeEach
    void setUp() {
        store = new InMemorySessionStore();
    }

    @Test
    void 타임아웃_지난_세션은_제거된다() throws InterruptedException {
        // spring container를 거치지 않고 new로 세션 만료 타임아웃 50ms로 설정
        IdleSessionReaper reaper = new IdleSessionReaper(store, Duration.ofMillis(50));

        ChatSessionKey key = new ChatSessionKey("user1", "sender1");
        store.getOrCreate(key);

        Thread.sleep(100); // 50ms 보다 길게
        reaper.reapIdleSessions();

        assertTrue(store.find(key).isEmpty()); // then : 세션 없음
    }

    @Test
    void 방금_활동한_세션은_제거되지_않는다() {
        IdleSessionReaper reaper = new IdleSessionReaper(store, Duration.ofMinutes(50));
        ChatSessionKey key = new ChatSessionKey("user1", "sender1");
        store.getOrCreate(key);

        reaper.reapIdleSessions();   // 방금 만들었으니 타임아웃엔 안 걸림

        assertTrue(store.find(key).isPresent()); // then : 세션 있음
    }

}