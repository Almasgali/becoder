package ru.becoder.krax.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import ru.becoder.krax.dto.AccountRequest;
import ru.becoder.krax.dto.AccountResponse;
import ru.becoder.krax.model.Account;
import ru.becoder.krax.repository.AccountRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class AccountService {
    private final AccountRepository accountRepository;

    public void createAccount(AccountRequest accountRequest) {
        if (accountRequest.getBalance() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance < 0");
        }

        Account account = Account.builder()
                .name(accountRequest.getName())
                .balance(accountRequest.getBalance())
                .build();

        accountRepository.save(account);
        log.info("Acconut {} is saved", Optional.of(account.getId()));
    }

    @Transactional
    public void increaseBalance(long id, @Min(0) long amount) {
        accountRepository.increaseBalance(id, amount);
    }

    @Transactional
    public void decreaseBalance(long id, @Min(0) long amount) {
        if (accountRepository.decreaseBalance(id, amount) == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough money");
        }
    }

    public AccountResponse getAccount(long id) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        return AccountResponse
                .builder()
                .id(account.getId())
                .name(account.getName())
                .balance(account.getBalance())
                .build();
    }
}
