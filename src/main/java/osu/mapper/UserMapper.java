package osu.mapper;

import osu.model.Project;
import osu.model.User;
import osu.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "projects", target = "projectIds")
    UserDto userToUserDto(User user);

    List<UserDto> usersToUserDtos(List<User> users);

    default Set<Long> mapProjectToProjectIds(Set<Project> projects) {
        return projects.stream()
                .map(Project::getRegistrationNumber)
                .collect(Collectors.toSet());
    }
}