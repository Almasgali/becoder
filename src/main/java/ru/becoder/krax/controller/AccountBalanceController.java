package ru.becoder.krax.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.becoder.krax.data.model.Account;
import ru.becoder.krax.service.AccountService;

@RestController
@RequiredArgsConstructor
public class AccountBalanceController extends AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PutMapping("/{id}/payment/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public void pay(@PathVariable Long id, @PathVariable long amount) {
        if (amount < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount < 0");
        }
        accountService.updateAccount(id, amount);
    }

    @PutMapping("/{id}/withdrawal/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@PathVariable Long id, @PathVariable long amount) {
        if (amount < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount < 0");
        }
        accountService.updateAccount(id, -amount);
    }
}
