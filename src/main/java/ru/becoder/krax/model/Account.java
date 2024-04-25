package ru.becoder.krax.model;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "accounts")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String name;
    private long balance;
}
