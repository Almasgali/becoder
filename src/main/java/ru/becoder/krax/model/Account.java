package ru.becoder.krax.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(value = "account")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Account {
    @Id
    String id;
    String name;
    long balance;
}