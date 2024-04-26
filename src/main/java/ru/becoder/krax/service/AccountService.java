package ru.becoder.krax.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
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
    public void increaseBalance(long id, @Min(0) long amount) {
        accountRepository.increaseBalance(id, amount);
    }

    @Transactional
    public void decreaseBalance(long id, @Min(0) long amount) {
        if (accountRepository.decreaseBalance(id, amount) == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough money");
        }
    }

    @Transactional
    public void updateAccount(long id, @Min(0) long balance) {
        accountRepository.changeBalance(id, balance);
    }

    public Account getAccount(long id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    public void deleteAccount(long id) {
        accountRepository.deleteById(id);
    }
}
