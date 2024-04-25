package ru.becoder.krax.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.becoder.krax.dto.AccountResponse;
import ru.becoder.krax.model.Account;
import ru.becoder.krax.repository.AccountRepository;

import static ru.becoder.krax.security.jwt.JwtUtils.getToken;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;

    public Account createAccount(String username) {
        if (accountRepository.findAccountByName(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        String token = getToken(username);
        Account account = Account.builder().name(username).token(token).build();
        return accountRepository.save(account);
    }

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
