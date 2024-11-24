package osu.user.mapper;

import org.mapstruct.Mapper;
import osu.auth.model.RegisterUserDto;
import osu.user.model.User;
import osu.user.model.UserDto;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    User toEntity(RegisterUserDto registerUserDto);

    List<UserDto> toDtos(List<User> users);
}