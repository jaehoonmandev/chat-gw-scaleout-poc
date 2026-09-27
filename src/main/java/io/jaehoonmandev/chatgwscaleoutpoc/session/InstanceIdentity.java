package io.jaehoonmandev.chatgwscaleoutpoc.session;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 서비스가 구동되고 있는 인스턴스의 ID
 */
@Component
public class InstanceIdentity {
    private final String id = UUID.randomUUID().toString();

    public String id() {return id;}
}
