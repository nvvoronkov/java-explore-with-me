package practicum.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import practicum.user.dto.NewUserDto;
import practicum.user.dto.UserDto;
import practicum.user.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    List<UserDto> listUserToListUserDto(List<User> user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User newUserRequestDtoToUser(NewUserDto newUserDto);
}