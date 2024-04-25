package ru.becoder.krax.repository;


import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.becoder.krax.model.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findById(@NonNull String id);
}
