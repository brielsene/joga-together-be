package br.com.joga_together.mapper;

import br.com.joga_together.dto.UserCreateRequestDto;
import br.com.joga_together.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateRequestDto dto);
}
