package ru.becoder.krax.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import ru.becoder.krax.dto.AccountRequest;
import ru.becoder.krax.exceptions.AccountAlreadyExistsException;
import ru.becoder.krax.exceptions.AccountNotFoundException;
import ru.becoder.krax.exceptions.NotEnoughMoneyException;
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
            throw new AccountAlreadyExistsException();
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
                .orElseThrow(AccountNotFoundException::new);
        long newBalance = account.getBalance() + amount;
        if (newBalance < 0) {
            throw new NotEnoughMoneyException();
        }
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    public Account getAccount(long id) {
        return accountRepository
                .findById(id)
                .orElseThrow(AccountNotFoundException::new);
    }

    public void deleteAccount(long id) {
        accountRepository.deleteById(id);
    }
}
