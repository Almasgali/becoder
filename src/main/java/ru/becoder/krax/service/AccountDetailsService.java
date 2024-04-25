package ru.becoder.krax.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.becoder.krax.repository.AccountRepository;

@Service
public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) {
        return userRepository.findAccountByName(name)
                .orElseThrow(() -> new UsernameNotFoundException(name));
    }
}