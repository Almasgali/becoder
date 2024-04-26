package ru.becoder.krax.controller;

import org.springframework.web.bind.annotation.RequestMapping;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody AccountRequest accountRequest) {
        accountService.createAccount(accountRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse getAccount(@PathVariable long id) {
        return accountService.getAccount(id);
    }

    @PutMapping("/{id}/payment/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public void payment(@PathVariable long id, @PathVariable @Min(0) long amount) {
        accountService.increaseBalance(id, amount);
    }

    @PutMapping("/{id}/withdrawal/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public void withdrawal(@PathVariable long id, @PathVariable @Min(0) long amount) {
        accountService.decreaseBalance(id, amount);
    }

}
