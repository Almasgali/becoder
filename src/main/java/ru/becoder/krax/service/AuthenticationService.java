package ru.becoder.krax.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import ru.becoder.krax.data.dto.AuthRequest;
import ru.becoder.krax.data.dto.JwtResponse;
import ru.becoder.krax.data.dto.RegisterRequest;
import ru.becoder.krax.data.mapper.JwtMapper;
import ru.becoder.krax.data.model.Account;
import ru.becoder.krax.repository.AccountRepository;
import ru.becoder.krax.security.role.Role;
import ru.becoder.krax.utils.JwtUtils;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthenticationService {

    private final AccountRepository accountRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final JwtMapper jwtMapper;

    public void createAccount(@Valid RegisterRequest request) {
        if (accountRepository.findAccountByName(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists: " + request.getUsername());
        }
        Account account = Account.builder()
                .name(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        accountRepository.save(account);
    }

    public JwtResponse generateAuthToken(@Valid AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        return jwtMapper.map(jwtUtils.getToken(user));
    }
}
