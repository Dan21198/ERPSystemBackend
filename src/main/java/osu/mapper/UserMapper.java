package osu.mapper;

import org.mapstruct.Mapper;
import osu.dto.UserDto;
import osu.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDto userDto);
    UserDto toDto(User user);
}