package ru.becoder.krax.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class ExcludeSecurityConfig extends InternalConfig {
}
