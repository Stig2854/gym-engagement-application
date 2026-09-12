package com.gym.engagement.app.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationConfigTest {

    private static final String RAW_PASSWORD = "temporaryPass1";

    private ApplicationConfig config;

    @BeforeEach
    void setUp() {
        config = new ApplicationConfig();
    }

    @Test
    void passwordEncoder_ShouldReturnBcryptPasswordEncoder() {
        PasswordEncoder actual = config.passwordEncoder();

        assertInstanceOf(BCryptPasswordEncoder.class, actual);
    }

    @Test
    void passwordEncoder_ShouldEncodeAndMatchRawPassword() {
        PasswordEncoder passwordEncoder = config.passwordEncoder();

        String actual = passwordEncoder.encode(RAW_PASSWORD);
        assertNotEquals(RAW_PASSWORD, actual);
        assertTrue(passwordEncoder.matches(RAW_PASSWORD, actual));
    }

    @Test
    void propertySourcesPlaceholderConfigurer_ShouldReturnNotNullInstance() {
        PropertySourcesPlaceholderConfigurer actual = ApplicationConfig.propertySourcesPlaceholderConfigurer();

        assertNotNull(actual);
        assertInstanceOf(PropertySourcesPlaceholderConfigurer.class, actual);
    }
}