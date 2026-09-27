package io.jaehoonmandev.chatgwscaleoutpoc.webhook;

import io.jaehoonmandev.chatgwscaleoutpoc.session.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebhookController.class)
class WebhookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ChatSessionService chatSessionService;

    @MockitoBean
    InstanceIdentity instanceIdentity;

    @Test
    void 존재하는_세션을_조회하면_200과_상태를_반환한다() throws Exception {
        ChatSessionKey key = new ChatSessionKey("sender1", "user1");
        ChatSession session = new ChatSession(key);   // 진짜 객체, 생성만 해도 state=CREATED

        // stub: "sessionStore.find(key)가 이 인자로 호출되면 Optional.of(session)을 리턴해라"
        given(chatSessionService.findSession("sender1", "user1")).willReturn(Optional.of(session));
        given(instanceIdentity.id()).willReturn("test-instance");

        mockMvc.perform(get("/api/sessions/{senderKey}/{userKey}", "sender1", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instanceId").value("test-instance"))
                .andExpect(jsonPath("$.state").value("CREATED"));
    }

    @Test
    void 없는_세션을_조회하면_404를_반환한다() throws Exception {
        // stub : "sessionStore.find(key)가 Optional.empty() 반환해라"
        given(chatSessionService.findSession(any(), any())).willReturn(Optional.empty());

        mockMvc.perform(get("/api/sessions/{senderKey}/{userKey}", "sender1", "user1"))
                .andExpect(status().isNotFound());
    }

}