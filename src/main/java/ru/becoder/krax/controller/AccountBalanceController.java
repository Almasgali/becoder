package ru.becoder.krax.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.becoder.krax.data.model.Account;
import ru.becoder.krax.service.AccountService;

@RestController
@RequiredArgsConstructor
public class AccountBalanceController extends AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccount(@PathVariable long id) {
        return accountService.getAccount(id);
    }

    @PutMapping("/{id}/payment/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public void pay(@PathVariable long id, @PathVariable @Min(0) long amount) {
        accountService.increaseBalance(id, amount);
    }

    @PutMapping("/{id}/withdrawal/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@PathVariable long id, @PathVariable @Min(0) long amount) {
        accountService.decreaseBalance(id, -amount);
    }
}
