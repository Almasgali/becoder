package ru.becoder.krax.security.role;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public enum Role {
    USER(Authority.API_USAGE),
    ADMIN(Authority.API_USAGE);

    private final Collection<? extends GrantedAuthority> authorities;

    Role(Authority... authorities) {
        this.authorities = Arrays.stream(authorities)
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
