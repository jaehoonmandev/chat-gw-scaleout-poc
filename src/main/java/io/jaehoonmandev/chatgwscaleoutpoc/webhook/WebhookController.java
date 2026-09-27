package io.jaehoonmandev.chatgwscaleoutpoc.webhook;

import io.jaehoonmandev.chatgwscaleoutpoc.session.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WebhookController {

    // 소스 수정 없이 전환하기 위해 구현체가 아니라 인터페이스로 받는다.
    private final ChatSessionService chatSessionService;
    private final InstanceIdentity instanceIdentity;

    public WebhookController(ChatSessionService chatSessionService,
                             InstanceIdentity instanceIdentity) {
        this.chatSessionService = chatSessionService;
        this.instanceIdentity = instanceIdentity;
    }

    // 메시지 이벤트 수신
    @PostMapping("/message")
    public ResponseEntity<ChatSessionResponse> receiveUserMessage(@RequestBody @Valid UserMessageRequest request) {
        ChatSession session = chatSessionService.handleMessage(request);
        return ResponseEntity.ok(new ChatSessionResponse(instanceIdentity.id(), session.getState()));
    }

    // 세션 상태 확인
    @GetMapping("/sessions/{senderKey}/{userKey}")
    public ResponseEntity<ChatSessionResponse> getSession(@PathVariable String senderKey, @PathVariable String userKey) {
        return chatSessionService.findSession(senderKey, userKey)
                .map(session -> ResponseEntity.ok(new ChatSessionResponse(instanceIdentity.id(), session.getState())))
                .orElse(ResponseEntity.notFound().build());
    }
}
