package ru.becoder.krax.data.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthRequest {
    @Size(min = 5, max = 50)
    String username;
    @Size(min = 1, max = 200)
    String password;
}
