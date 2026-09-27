package io.jaehoonmandev.chatgwscaleoutpoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ChatGwScaleoutPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatGwScaleoutPocApplication.class, args);
    }

}
