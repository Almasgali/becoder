package ru.becoder.krax.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.becoder.krax.dto.AccountRequest;
import ru.becoder.krax.model.Account;
import ru.becoder.krax.repository.AccountRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
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

    public void updateAccount(String id, long amount) {
        Optional<Account> optAccount = accountRepository.findById(id);
        if (optAccount.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found");
        }
        Account account = optAccount.get();
        if (account.getBalance() + amount < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough money");
        }
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }
}
