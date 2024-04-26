package ru.becoder.krax.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@EnableAutoConfiguration
@TestPropertySource(
        locations = "classpath:application.properties")
public class InternalConfig {
}
