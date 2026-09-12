package com.gym.engagement.app.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
class StorageDataInitializerTest {

    private static final Long TRAINEE_ID = 1L;
    private static final Long TRAINER_ID = 2L;
    private static final Long TRAINING_ID = 1L;

    private static final String FILE_DESCRIPTION = "test-data.csv";
    private static final String INITIALIZATION_FAILED_MESSAGE = "Failed to initialize in-memory storage";
    private static final String BEAN_NAME = "inMemoryStorage";

    @Mock
    private InitialDataParser initialDataParser;

    @Mock
    private Resource initialDataFile;

    @Mock
    private InMemoryStorage storage;

    private StorageDataInitializer initializer;

    @BeforeEach
    void setUp() {
        initializer = new StorageDataInitializer(initialDataParser);
        initializer.setInitialDataFile(initialDataFile);
    }

    @Test
    void postProcessAfterInitialization_ShouldInitializeStorage_WhenBeanIsInMemoryStorage() throws IOException {
        Trainee trainee = createTrainee();
        Trainer trainer = createTrainer();
        Training training = createTraining();

        String fileContent = "TRAINEE,1,...\nTRAINER,2,...\nTRAINING,1,...";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Map<Long, Trainee> traineeMap = new HashMap<>();
        Map<Long, Trainer> trainerMap = new HashMap<>();
        Map<Long, Training> trainingMap = new HashMap<>();

        when(initialDataFile.getInputStream()).thenReturn(inputStream);
        when(initialDataFile.getDescription()).thenReturn(FILE_DESCRIPTION);
        when(initialDataParser.parse("TRAINEE,1,...")).thenReturn(trainee);
        when(initialDataParser.parse("TRAINER,2,...")).thenReturn(trainer);
        when(initialDataParser.parse("TRAINING,1,...")).thenReturn(training);
        when(storage.getTrainees()).thenReturn(traineeMap);
        when(storage.getTrainers()).thenReturn(trainerMap);
        when(storage.getTrainings()).thenReturn(trainingMap);

        Object actual = initializer.postProcessAfterInitialization(storage, BEAN_NAME);

        assertSame(storage, actual);
        assertEquals(1, traineeMap.size());
        assertEquals(1, trainerMap.size());
        assertEquals(1, trainingMap.size());
        assertSame(trainee, traineeMap.get(TRAINEE_ID));
        assertSame(trainer, trainerMap.get(TRAINER_ID));
        assertSame(training, trainingMap.get(TRAINING_ID));
    }

    @Test
    void postProcessAfterInitialization_ShouldReturnBeanUnchanged_WhenBeanIsNotInMemoryStorage() {
        Object otherBean = new Object();

        Object actual = initializer.postProcessAfterInitialization(otherBean, "someOtherBean");

        assertSame(otherBean, actual);
    }

    @Test
    void postProcessAfterInitialization_ShouldSkipBlankLines() throws IOException {
        String fileContent = "TRAINEE,1,...\n\n   \nTRAINER,2,...";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Trainee trainee = createTrainee();
        Trainer trainer = createTrainer();

        when(initialDataFile.getInputStream()).thenReturn(inputStream);
        when(initialDataFile.getDescription()).thenReturn(FILE_DESCRIPTION);
        when(initialDataParser.parse("TRAINEE,1,...")).thenReturn(trainee);
        when(initialDataParser.parse("TRAINER,2,...")).thenReturn(trainer);
        when(storage.getTrainees()).thenReturn(new HashMap<>());
        when(storage.getTrainers()).thenReturn(new HashMap<>());
        when(storage.getTrainings()).thenReturn(new HashMap<>());

        initializer.postProcessAfterInitialization(storage, BEAN_NAME);

        verify(initialDataParser, times(2)).parse(any());
        verify(initialDataParser, never()).parse("");
        verify(initialDataParser, never()).parse("   ");
    }

    @Test
    void postProcessAfterInitialization_ShouldThrowException_WhenIOExceptionOccurs() throws IOException {
        when(initialDataFile.getInputStream()).thenThrow(new IOException("File not found"));
        when(initialDataFile.getDescription()).thenReturn(FILE_DESCRIPTION);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> initializer.postProcessAfterInitialization(storage, BEAN_NAME));

        assertEquals(INITIALIZATION_FAILED_MESSAGE, exception.getMessage());
    }

    @Test
    void postProcessAfterInitialization_ShouldThrowException_WhenParserThrowsRuntimeException() throws IOException {
        String fileContent = "INVALID_LINE";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        when(initialDataFile.getInputStream()).thenReturn(inputStream);
        when(initialDataFile.getDescription()).thenReturn(FILE_DESCRIPTION);
        when(initialDataParser.parse("INVALID_LINE")).thenThrow(new IllegalArgumentException("Unsupported initial data record type"));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> initializer.postProcessAfterInitialization(storage, BEAN_NAME));

        assertEquals(INITIALIZATION_FAILED_MESSAGE, exception.getMessage());
    }

    private Trainee createTrainee() {
        return Trainee.builder()
                .userId(TRAINEE_ID)
                .build();
    }

    private Trainer createTrainer() {
        return Trainer.builder()
                .userId(TRAINER_ID)
                .build();
    }

    private Training createTraining() {
        return Training.builder()
                .id(TRAINING_ID)
                .build();
    }
}