package ru.becoder.krax.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.becoder.krax.dto.AccountRequest;
import ru.becoder.krax.dto.AccountResponse;
import ru.becoder.krax.model.Account;
import ru.becoder.krax.repository.AccountRepository;

@Service
@RequiredArgsConstructor
@Async
@Slf4j
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
        log.info("Acconut {} is saved", account.getId());
    }

    @Transactional
    public void updateAccount(String id, long amount) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        long newBalance = account.getBalance() + amount;
        if (newBalance < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough money");
        }
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    public AccountResponse getAccount(String id) {
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
