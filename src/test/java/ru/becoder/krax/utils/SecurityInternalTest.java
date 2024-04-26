package ru.becoder.krax.utils;

import org.springframework.context.annotation.Import;
import ru.becoder.krax.configuration.InternalConfig;

@Import(
        InternalConfig.class
)
public class SecurityInternalTest extends InternalTest {
}
