package io.jaehoonmandev.chatgwscaleoutpoc.webhook;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * 고객이 보낸 메시지 이벤트 객체
 */
public class UserMessageRequest{

    @NotBlank(message = "userKey는 필수")
    private String userKey;
    @NotBlank(message = "senderKey는 필수")
    private String senderKey;
    private LocalDateTime sentTime;
    @NotBlank(message = "message는 필수")
    private String message;

    public String getMessage() {
        return message;
    }

    public String getUserKey() {
        return userKey;
    }

    public String getSenderKey() {
        return senderKey;
    }
}
