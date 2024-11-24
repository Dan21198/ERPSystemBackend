package osu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import osu.dto.auth.RegisterUserDto;
import osu.dto.auth.UserDto;
import osu.model.User;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    User toEntity(RegisterUserDto registerUserDto);

    List<UserDto> toDtos(List<User> users);
}