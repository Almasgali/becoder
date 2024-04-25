package ru.becoder.krax.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import ru.becoder.krax.data.model.Account;
import ru.becoder.krax.repository.AccountRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional
    public void updateAccount(Long id, long amount) {
        Account account = accountRepository
                .findForUpdateById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        long newBalance = account.getBalance() + amount;
        if (newBalance < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough money");
        }
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    public Account getAccount(Long id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }
}
