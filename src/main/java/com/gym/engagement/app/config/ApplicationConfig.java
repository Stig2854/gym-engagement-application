package com.gym.engagement.app.config;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.gym.engagement.app")
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Map<String, Trainee> traineeStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, Trainer> trainerStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, Training> trainingStorage() {
        return new HashMap<>();
    }


}