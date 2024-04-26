package ru.becoder.krax.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get account by its id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found account."),
            @ApiResponse(responseCode = "404", description = "Account not found.")
    })
    public Account getAccount(
            @PathVariable
            @Parameter(description = "Id of account.")
            long id) {
        return accountService.getAccount(id);
    }

    @PutMapping("/{id}/payment/{amount}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Increase balance of specified account.")
    public void pay(
            @PathVariable
            @Parameter(description = "Id of account.")
            long id,
            @PathVariable
            @Min(0)
            @Parameter(description = "Increase amount.")
            long amount) {
        accountService.increaseBalance(id, amount);
    }

    @Operation(summary = "Decrease balance of specified account.")
    @PutMapping("/{id}/withdrawal/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(
            @PathVariable
            @Parameter(description = "Id of account.")
            Long id,
            @PathVariable
            @Min(0)
            @Parameter(description = "Decrease amount.")
            long amount) {
        accountService.decreaseBalance(id, amount);
    }
}
