package ru.becoder.krax.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.becoder.krax.data.dto.AuthRequest;
import ru.becoder.krax.data.dto.JwtResponse;
import ru.becoder.krax.data.dto.RegisterRequest;
import ru.becoder.krax.service.AuthenticationService;

@RequiredArgsConstructor
@RestController
public class AuthenticationController extends AccountController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creating new account.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created."),
            @ApiResponse(responseCode = "400", description = "Account already exists.")
    })
    public void createAccount(
            @Parameter(description = "Providing name of new account.")
            @RequestBody @Valid RegisterRequest request) {
        authenticationService.createAccount(request);
    }

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse getToken(@RequestBody @Valid AuthRequest request) {
        return authenticationService.generateAuthToken(request);
    }
}