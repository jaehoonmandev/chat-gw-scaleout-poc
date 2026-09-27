package io.jaehoonmandev.chatgwscaleoutpoc.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 미사용 세션 정리
 */
@Component
public class IdleSessionReaper {

    private final ChatSessionStore chatSessionStore;
    private final Duration idleTimeout; // 무응답 timeout 시간

    public IdleSessionReaper(
            ChatSessionStore chatSessionStore,
            // 무응답 timeout 시간은 외부 설정값 우선 사용, Default 5분
            @Value("${chatgw.session.idle-timeout:PT5M}") Duration idleTimeout
    ) {
        this.chatSessionStore = chatSessionStore;
        this.idleTimeout = idleTimeout;
    }

    // 3분마다 만료 세션 정리; 메인 애플리케이션에 @EnableScheduling 설정
    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
    public void reapIdleSessions() {
        // 현재 시간에서 timeout 만큼 뺀 시간
        LocalDateTime cutOff = LocalDateTime.now().minus(idleTimeout);

        for (ChatSession chatSession : chatSessionStore.findAll()) {
            // 만료 시간 이전 세션은 제거
            if (chatSession.getLastMessageAt().isBefore(cutOff)) {
                chatSessionStore.remove(chatSession.getSessionKey());
            }
        }
    }

}
