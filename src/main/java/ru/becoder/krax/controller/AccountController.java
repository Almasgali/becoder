package ru.becoder.krax.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.becoder.krax.dto.AccountRequest;
import ru.becoder.krax.model.Account;
import ru.becoder.krax.service.AccountService;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creating new account.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created."),
            @ApiResponse(responseCode = "400", description = "Account already exists.")
    })
    public Account createAccount(
            @RequestBody
            @Parameter(description = "Providing name of new account.")
            AccountRequest accountRequest) {
        return accountService.createAccount(accountRequest);
    }

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
            Long id) {
        return accountService.getAccount(id);
    }

    @PutMapping("/{id}/payment/{amount}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Increase balance of specified account.")
    public void payment(
            @PathVariable
            @Parameter(description = "Id of account.")
            Long id,
            @PathVariable
            @Min(0)
            @Parameter(description = "Increase amount.")
            long amount) {
        accountService.updateAccount(id, amount);
    }

    @PutMapping("/{id}/withdrawal/{amount}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Decrease balance of specified account.")
    public void withdrawal(
            @PathVariable
            @Parameter(description = "Id of account.")
            Long id,
            @PathVariable
            @Min(0)
            @Parameter(description = "Decrease amount.")
            long amount) {
        accountService.updateAccount(id, -amount);
    }
}
