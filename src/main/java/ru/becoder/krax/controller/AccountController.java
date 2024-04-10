package ru.becoder.krax.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.becoder.krax.dto.AccountRequest;
import ru.becoder.krax.service.AccountService;

@RestController
@RequestMapping("/api/v0/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody AccountRequest accountRequest) {
        accountService.createAccount(accountRequest);
    }

    @PutMapping("/{id}/payment/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public void payment(@PathVariable String id, @PathVariable long amount) {
        accountService.updateAccount(id, amount);
    }

    @PutMapping("/{id}/withdrawal/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public void withdrawal(@PathVariable String id, @PathVariable long amount) {
        accountService.updateAccount(id, -amount);
    }
}
