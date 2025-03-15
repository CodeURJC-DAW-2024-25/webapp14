package es.codeurjc.webapp14.mapper;

import org.springframework.stereotype.Component;
import es.codeurjc.webapp14.dto.UserDTO;
import es.codeurjc.webapp14.model.User;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        dto.setBanned(user.getBanned());
        dto.setRoles(user.getRoles());

        return dto;
    }
}
