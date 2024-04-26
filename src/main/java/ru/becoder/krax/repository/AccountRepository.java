package ru.becoder.krax.repository;


import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.Min;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.becoder.krax.data.model.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findForUpdateById(long id);

    Optional<Account> findByName(String name);
  
    @Query(
            value = "UPDATE account SET balance = balance + ?2 WHERE id = ?1",
            nativeQuery = true
    )
    @Modifying
    int increaseBalance(long id, @Min(0) long amount);

    @Query(
            value = "UPDATE account SET balance = balance - ?2 WHERE id = ?1 AND balance >= ?2",
            nativeQuery = true
    )
    @Modifying
    int decreaseBalance(long id, @Min(0) long amount);

    @Query(
            value = "UPDATE account SET balance = ?2 WHERE id = ?1",
            nativeQuery = true
    )
    @Modifying
    int changeBalance(long id, @Min(0) long balance);
}
