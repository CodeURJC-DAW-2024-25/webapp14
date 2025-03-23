package es.codeurjc.webapp14.dto;

public record NewUserDTO(
    String name,
    String surname,
    String email,
    String encodedPassword,
    String confirmPassword
) {}