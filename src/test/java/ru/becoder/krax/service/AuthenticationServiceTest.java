package ru.becoder.krax.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.server.ResponseStatusException;
import ru.becoder.krax.data.dto.AuthRequest;
import ru.becoder.krax.data.dto.JwtResponse;
import ru.becoder.krax.data.dto.RegisterRequest;
import ru.becoder.krax.utils.SecurityInternalTest;
import ru.becoder.krax.utils.VariableColumnFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AuthenticationServiceTest extends SecurityInternalTest {
    private static final String DEFAULT_USERNAME = "test_user";
    private static final String DEFAULT_PASSWORD = "password";
    @Autowired
    private AuthenticationService authenticationService;

    public static Stream<Arguments> getWrongAuthCredentials() {
        return Stream.of(
                Arguments.of("Small username", "", DEFAULT_PASSWORD, ConstraintViolationException.class),
                Arguments.of("Small password", DEFAULT_USERNAME, "", ConstraintViolationException.class),
                Arguments.of(
                        "Too big username",
                        "1".repeat(100), DEFAULT_PASSWORD,
                        ConstraintViolationException.class
                ),
                Arguments.of(
                        "Wrong username",
                        "wrong_name", DEFAULT_PASSWORD,
                        BadCredentialsException.class
                ),
                Arguments.of(
                        "Wrong password",
                        DEFAULT_USERNAME, "wrong_pass",
                        BadCredentialsException.class
                )
        );
    }

    private static RegisterRequest getRegister(String username) {
        return RegisterRequest.builder()
                .username(username)
                .password(DEFAULT_PASSWORD)
                .build();
    }

    private static RegisterRequest getRegister() {
        return getRegister(DEFAULT_USERNAME);
    }

    private static AuthRequest getAuth(String username, String password) {
        return RegisterRequest.builder()
                .username(username)
                .password(password)
                .build();
    }

    private static AuthRequest getAuth(String username) {
        return getAuth(username, DEFAULT_PASSWORD);
    }

    private static AuthRequest getAuth() {
        return getAuth(DEFAULT_USERNAME);
    }

    @Test
    @ExpectedDatabase(
            value = "classpath:database/controller/create/single_account.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT,
            columnFilters = VariableColumnFilter.class
    )
    void whenCreateNewAccountThanAllIsOk() {
        createDefault();
    }

    @Test
    @DatabaseSetup(
            value = "classpath:database/controller/create/single_account.xml"
    )
    @ExpectedDatabase(
            value = "classpath:database/controller/create/single_account.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT,
            columnFilters = VariableColumnFilter.class
    )
    void whenCreateExistingAccountThanNothingHappen() {
        assertThatThrownBy(this::createDefault)
                .isInstanceOf(ResponseStatusException.class)
                .extracting("status")
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @ExpectedDatabase(
            value = "classpath:database/controller/create/single_account.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT,
            columnFilters = VariableColumnFilter.class
    )
    void whenParallelCreateAccountThanAccountCreatesOnce() throws InterruptedException {
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        RegisterRequest request = getRegister();
        int requests = 100;

        List<? extends Future<?>> tasks = Stream.<Runnable>generate(() ->
                        () -> authenticationService.createAccount(request)
                )
                .limit(requests)
                .map(executorService::submit)
                .toList();
        List<ResponseStatusException> exceptions = new ArrayList<>(requests);
        boolean wasBadException = false;
        for (Future<?> task : tasks) {
            try {
                task.get();
            } catch (ExecutionException e) {
                if (e.getCause() instanceof ResponseStatusException responseStatusException) {
                    exceptions.add(responseStatusException);
                } else {
                    wasBadException = true;
                }
            }
        }
        assertThat(wasBadException).isFalse();
        assertThat(exceptions)
                .allMatch(e -> e.getStatusCode() == HttpStatus.BAD_REQUEST)
                .extracting("size")
                .isEqualTo(requests - 1);
    }

    @Test
    @ExpectedDatabase(
            value = "classpath:database/controller/create/many_accounts.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT,
            columnFilters = VariableColumnFilter.class
    )
    void whenCreateManyAccountsThanAllIsOk() {
        Stream.of("one__", "two__", "three")
                .map(AuthenticationServiceTest::getRegister)
                .forEach(authenticationService::createAccount);
    }

    @Test
    void whenAuthViaExistingAccountThanShouldReturnToken() {
        createDefault();
        assertThat(authenticationService.generateAuthToken(getAuth()))
                .isNotNull()
                .extracting(JwtResponse::getToken)
                .isNotNull();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getWrongAuthCredentials")
    void whenAuthByWrongCredentialsThanGetUnauthorized(
            String displayName, String username, String password, Class<?> exception
    ) {
        createDefault();
        assertThatThrownBy(() -> authenticationService.generateAuthToken(getAuth(username, password)))
                .isInstanceOfAny(exception);
    }

    private void createDefault() {
        authenticationService.createAccount(getRegister());
    }
}
