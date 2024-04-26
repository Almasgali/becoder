package ru.becoder.krax.data.mapper;

import org.mapstruct.Mapper;
import ru.becoder.krax.data.dto.JwtResponse;

@Mapper(componentModel = "spring")
public interface JwtMapper {

    JwtResponse map(String token);
}
