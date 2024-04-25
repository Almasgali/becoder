package ru.becoder.krax.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import ru.becoder.krax.dto.AccountRequest;
import ru.becoder.krax.model.Account;
import ru.becoder.krax.repository.AccountRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class AccountService {
    private final AccountRepository accountRepository;

    public Account createAccount(AccountRequest accountRequest) {

        if (accountRepository.findAccountByName(accountRequest.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.OK, "Account already exists");
        }

        Account account = Account.builder()
                .name(accountRequest.getName())
                .balance(0)
                .build();

        accountRepository.save(account);
        log.info("Acconut {} is saved", Optional.of(account.getId()));
        return account;
    }

    @Transactional
    public void updateAccount(long id, long amount) {
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

    public Account getAccount(long id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    public void deleteAccount(long id) {
        accountRepository.deleteById(id);
    }
}
