package es.codeurjc.webapp14.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import es.codeurjc.webapp14.dto.UserReportedDTO;
import es.codeurjc.webapp14.model.User;

@Mapper(componentModel = "spring")
public interface UserReportedMapper {

    UserReportedDTO toDTO(User user);

    User toDomain(UserReportedDTO userDTO);

    List<UserReportedDTO> toDTOs(List<User> users);

}