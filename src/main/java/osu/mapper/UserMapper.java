package osu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import osu.dto.auth.UserDto;
import osu.model.User;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);

    List<UserDto> usersToUserDtos(List<User> users);
}