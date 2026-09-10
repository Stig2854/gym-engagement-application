package com.gym.engagement.app.storage;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class StorageDataInitializer implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageDataInitializer.class);

    private final InitialDataParser initialDataParser;

    @Setter(onMethod_ = @Value("${storage.initial-data.path}"))
    private Resource initialDataFile;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof InMemoryStorage storage) {
            initializeStorage(storage);
        }

        return bean;
    }

    private void initializeStorage(InMemoryStorage storage) {
        LOGGER.info("Initializing in-memory storage from file: {}",
                initialDataFile.getDescription());

        try (BufferedReader reader = createReader()) {
            reader.lines()
                    .filter(line -> !line.isBlank())
                    .map(initialDataParser::parse)
                    .forEach(entity -> addToStorage(storage, entity));

            LOGGER.info("Initialized in-memory storage with {} trainees, {} trainers and {} trainings",
                    storage.getTrainees().size(),
                    storage.getTrainers().size(),
                    storage.getTrainings().size());
        } catch (IOException | RuntimeException exception) {
            LOGGER.error("Failed to initialize in-memory storage from file: {}",
                    initialDataFile.getDescription(),
                    exception);

            throw new IllegalStateException("Failed to initialize in-memory storage", exception);
        }
    }

    private BufferedReader createReader() throws IOException {
        InputStreamReader reader = new InputStreamReader(initialDataFile.getInputStream(), StandardCharsets.UTF_8);

        return new BufferedReader(reader);
    }

    private void addToStorage(InMemoryStorage storage, Object entity) {
        if (entity instanceof Trainee trainee) {
            storage.getTrainees().put(trainee.getUserId(), trainee);
        } else if (entity instanceof Trainer trainer) {
            storage.getTrainers().put(trainer.getUserId(), trainer);
        } else if (entity instanceof Training training) {
            storage.getTrainings().put(training.getId(), training);
        }
    }
}