package io.jaehoonmandev.chatgwscaleoutpoc.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class InMemorySessionStoreTest {

    private InMemorySessionStore store;

    @BeforeEach
    void setUp() {
        store = new InMemorySessionStore();
    }

    @Test
    void 같은_키로_두번_호출하면_같은_세션을_반환한다(){

        // 같은 키로 세션을 중복으로 생성 시도
        ChatSessionKey chatSessionKey = new ChatSessionKey("sender1", "user1");

        ChatSession session1 = store.getOrCreate(chatSessionKey);
        ChatSession session2 = store.getOrCreate(chatSessionKey);

        //생성한 객체가 같은 인스턴스라면 통과
        assertSame(session1, session2);
    }

    @Test
    void 다른_키로_각각_호출하면_다른_세션을_반환한다(){

        // 키가 다른 각각의 세션을 생성
        ChatSessionKey chatSessionKey1 = new ChatSessionKey("sender1", "user1");
        ChatSessionKey chatSessionKey2 = new ChatSessionKey("sender2", "user2");

        ChatSession session1 = store.getOrCreate(chatSessionKey1);
        ChatSession session2 = store.getOrCreate(chatSessionKey2);

        // 각 세션이 같은 인스턴스가 아니라면 통과
        assertNotSame(session1, session2);
    }

    @Test
    void 동시에_같은_키로_요청해도_세션은_하나만_생성된다() throws InterruptedException {

        ChatSessionKey chatSessionKey = new ChatSessionKey("sender1", "user1");
        int threadCount = 50; // 동시에 요청을 실행할 개수

        // 스레드를 미리 만들어서 대기.
        // new Thread(...).start()를 직접하는 것과 결과는 비슷하지만, 스레드 생성/관리를 알아서 해줌.
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // 카운트 다운
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        Set<ChatSession> results = ConcurrentHashMap.newKeySet(); // 중복 제거

        // 스레드 풀에 작업을 대기
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try{
                    startLatch.await(); // 스레드 대기

                    ChatSession session = store.getOrCreate(chatSessionKey);
                    results.add(session);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }finally{
                    doneLatch.countDown();
                }
            });
        }
        // 1로 설정된 카운트를 다운 시켜 await 중이던 스레드를 일괄 실행하여 getOrCreate() 실행
        startLatch.countDown();

        doneLatch.await(); // 모두 실행 될 떄까지 대기
        executor.shutdown(); // 작업 종료 후 스레드풀 정리

        // 최종적으로 생성된 세션의 개수가 1개라면 통과
        assertEquals(1, results.size());
    }

    @Test
    void 없는_키를_조회하면_비어있다(){
        Optional<ChatSession> result = store.find(new ChatSessionKey("sender1", "user1"));
        assertTrue(result.isEmpty());
    }

    @Test
    void 있는_키를_조회하면_getOrCreate로_만든_것과_같은_객체를_반환한다(){
        ChatSessionKey chatSessionKey = new ChatSessionKey("sender1", "user1");

        ChatSession created = store.getOrCreate(chatSessionKey);

        ChatSession found = store.find(chatSessionKey).get();

        assertSame(created, found);
    }

    @Test
    void remove_이후_find는_비어있다(){
        ChatSessionKey chatSessionKey = new ChatSessionKey("sender1", "user1");

        store.getOrCreate(chatSessionKey);
        store.remove(chatSessionKey);
        assertTrue(store.find(chatSessionKey).isEmpty());
    }

}