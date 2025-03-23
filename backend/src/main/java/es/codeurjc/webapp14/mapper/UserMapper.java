package es.codeurjc.webapp14.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import es.codeurjc.webapp14.dto.UserDTO;
import es.codeurjc.webapp14.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserDTO toDTO(User user);

    User toDomain(UserDTO userDTO);

    List<UserDTO> toDTOs(List<User> users);
}
