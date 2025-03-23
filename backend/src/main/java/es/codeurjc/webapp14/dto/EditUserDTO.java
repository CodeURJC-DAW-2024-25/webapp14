package es.codeurjc.webapp14.dto;


public record EditUserDTO(
        String name,
        String surname,
        String email,
        String address,
        String currentPassword,
        String newPassword,
        String confirmPassword)
        //MultipartFile newImage) 
        {
}