package com.gym.engagement.app;

import com.gym.engagement.app.config.ApplicationConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class GymCrmApplication {

    private GymCrmApplication() {
    }

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ApplicationConfig.class)) {
            // Spring application context is initialized here.
        }
    }
}