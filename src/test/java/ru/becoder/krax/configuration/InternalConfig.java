package ru.becoder.krax.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@TestPropertySource(
        locations = "classpath:application.properties")
public class InternalConfig {
}
