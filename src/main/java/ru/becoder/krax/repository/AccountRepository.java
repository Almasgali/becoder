package ru.becoder.krax.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.becoder.krax.model.Account;

public interface AccountRepository extends MongoRepository<Account, String> {

}
