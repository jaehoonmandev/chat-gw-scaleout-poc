package io.jaehoonmandev.chatgwscaleoutpoc.session;

/**
 * 외부 호출 시 Response 객체
 * @param instanceId    이벤트를 처리한 인스턴스 ID
 * @param state         세션 상태
 */
public record ChatSessionResponse(String instanceId, ChatSessionState state) {}