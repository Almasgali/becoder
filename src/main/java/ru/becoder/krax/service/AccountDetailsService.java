package ru.becoder.krax.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.becoder.krax.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {
    private final AccountRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) {
        return userRepository.findAccountByName(name)
                .orElseThrow(() -> new UsernameNotFoundException(name));
    }
}