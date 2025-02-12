package ru.nirs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ApplicationTest {

    @Value("${logging.level.org.springframework}")
    private String overriddenProperty;

    @Value("${logging.level.ru.nirs}")
    private String defaultProperty;

    @Test
    void bootstrapProperty() {
        assertEquals("DEBUG", overriddenProperty);
        assertEquals("INFO", defaultProperty);
    }

}
