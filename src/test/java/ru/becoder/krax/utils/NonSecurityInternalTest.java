package ru.becoder.krax.utils;

import org.springframework.context.annotation.Import;
import ru.becoder.krax.configuration.ExcludeSecurityConfig;

@Import(
        ExcludeSecurityConfig.class
)
public class NonSecurityInternalTest extends InternalTest {
}
