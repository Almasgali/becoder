package ru.becoder.krax.service;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.becoder.krax.utils.NonSecurityInternalTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class AccountServiceTest extends NonSecurityInternalTest {

    private static final long ACCOUNT_ID = 1;
    @Autowired
    private AccountService accountService;

    @ParameterizedTest
    @ValueSource(longs = {0, 100, 200, 300})
    @DatabaseSetup(
            value = "classpath:database/controller/create/single_account.xml"
    )
    void whenDepositOnEmptyAccountThenBalanceEqualsDifference(long balanceChange) {
        balanceIncrease(balanceChange);
        assertBalance(balanceChange);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 100, 200, 300})
    @DatabaseSetup(
            value = "classpath:database/controller/create/single_account.xml"
    )
    @DatabaseSetup(
            value = "classpath:database/controller/create/account_with_balance.xml",
            type = DatabaseOperation.UPDATE
    )
    void whenAddBalanceToAccountThanItIncreaseOnAmount(long balanceChange) {
        long oldBalance = getBalance();
        balanceIncrease(balanceChange);
        assertBalance(oldBalance + balanceChange);
    }

    @Test
    @DatabaseSetup(
            value = "classpath:database/controller/create/single_account.xml"
    )
    @ExpectedDatabase(
            value = "classpath:database/controller/create/single_account.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    void whenTryDecreaseByIncreaseThenThrow() {
        assertThatThrownBy(() -> balanceIncrease(-100));
    }

    @Test
    @DatabaseSetup(
            value = "classpath:database/controller/create/single_account.xml"
    )
    @ExpectedDatabase(
            value = "classpath:database/controller/create/single_account.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    void whenDecreaseFromEmptyBalanceThenNothingHappen() {
        assertThatThrownBy(() -> balanceDecrease(100))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("status")
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DatabaseSetup(
            value = "classpath:database/controller/create/single_account.xml"
    )
    @ExpectedDatabase(
            value = "classpath:database/controller/create/single_account.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    void whenIncreaseBalanceByDecreaseThanThrow() {
        assertThatThrownBy(() -> balanceDecrease(-100));
    }

    @ParameterizedTest
    @ValueSource(
            longs = {0, 100, 200, 300}
    )
    @DatabaseSetup(
            value = "classpath:database/controller/create/single_account.xml"
    )
    @DatabaseSetup(
            value = "classpath:database/controller/create/account_with_balance.xml",
            type = DatabaseOperation.UPDATE
    )
    void whenDecreaseBalanceThanItDecreasedOnValue(long balanceChange) {
        long balance = getBalance();
        balanceDecrease(balanceChange);
        assertBalance(balance - balanceChange);
    }

    @Test
    @DatabaseSetup(
            value = "classpath:database/controller/create/single_account.xml"
    )
    void whenMultipleAddAndDecreaseThanBalanceIsCorrect() {
        long maxValue = 100;
        LongStream.rangeClosed(1, maxValue).forEach(v -> {
            balanceIncrease(v);
            balanceDecrease(v - 1);
        });
        assertBalance(maxValue);
    }

    @Test
    @DatabaseSetup(
            value = "classpath:database/controller/create/single_account.xml"
    )
    void whenParallelIncreaseBalanceThanBalanceIsCorrect() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        List<Integer> values = IntStream.rangeClosed(1, 5000)
                .sorted()
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        long res = values.stream().reduce(0, Integer::sum);

        List<? extends Future<?>> futures = values.stream()
                .<Runnable>map(v -> () -> balanceIncrease(v))
                .map(executor::submit)
                .toList();

        for (Future<?> future : futures) {
            future.get();
        }

        assertBalance(res);
    }

    @Test
    @DatabaseSetup(
            value = "classpath:database/controller/create/single_account.xml"
    )
    @DatabaseSetup(
            value = "classpath:database/controller/create/account_with_balance.xml",
            type = DatabaseOperation.UPDATE
    )
    void whenParallelDecreaseBalanceThanBalanceIsCorrect() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        List<Integer> values = IntStream.rangeClosed(1, 5000)
                .sorted()
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        long res = getBalance() - values.stream().reduce(0, Integer::sum);

        List<? extends Future<?>> futures = values.stream()
                .<Runnable>map(v -> () -> balanceDecrease(v))
                .map(executor::submit)
                .toList();

        for (Future<?> future : futures) {
            future.get();
        }

        assertBalance(res);
    }

    @Test
    @DatabaseSetup(
            value = "classpath:database/controller/create/single_account.xml"
    )
    @DatabaseSetup(
            value = "classpath:database/controller/create/account_with_balance.xml",
            type = DatabaseOperation.UPDATE
    )
    void whenParallelChangeBalanceThanBalanceIsCorrect() throws ExecutionException, InterruptedException {
        long res = getBalance();
        Predicate<Long> decreaseOn = i -> i % 3 == 0;

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        List<Long> values = LongStream.rangeClosed(1, 5000).boxed().toList();

        for (long value : values) {
            res += (decreaseOn.test(value) ? -1 : 1) * value;
        }

        List<? extends Future<?>> futures = Stream.<Runnable>concat(
                values.stream().filter(decreaseOn.negate()).map(v -> () -> balanceIncrease(v)),
                values.stream().filter(decreaseOn).map(v -> () -> balanceDecrease(v))
        ).map(executor::submit).toList();

        for (Future<?> future : futures) {
            future.get();
        }

        assertBalance(res);
    }

    private long getBalance() {
        return accountService.getAccount(ACCOUNT_ID).getBalance();
    }

    private void assertBalance(long balance) {
        assertThat(getBalance())
                .isEqualTo(balance);
    }

    private void balanceIncrease(long diff) {
        accountService.updateAccount(ACCOUNT_ID, diff);
    }

    private void balanceDecrease(long diff) {
        accountService.updateAccount(ACCOUNT_ID, -diff);
    }

}
