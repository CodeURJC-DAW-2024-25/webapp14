package es.codeurjc.webapp14.service;

import es.codeurjc.webapp14.dto.EditUserDTO;
import es.codeurjc.webapp14.dto.NewUserDTO;
import es.codeurjc.webapp14.dto.UserDTO;
import es.codeurjc.webapp14.mapper.UserMapper;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return toDTO(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public List<User> getAllRegularUsers() {
        return userRepository.findByRolesContaining("USER");
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getAdmin() {
        return userRepository.findByRolesContaining("ADMIN").stream().findFirst();
    }

    public void saveAdmin(User admin) {
        Optional<User> existing = getAdmin();
        if (!existing.isPresent()) {
            userRepository.save(admin);
        } else {
            User existingAdmin = existing.get();
            existingAdmin.setName(admin.getName());
            existingAdmin.setSurname(admin.getSurname());
            existingAdmin.setEmail(admin.getEmail());
            existingAdmin.setEncodedPassword(admin.getEncodedPassword());
            if (admin.getProfileImage() != null && !admin.getProfileImage().equals(existingAdmin.getProfileImage())) {
                existingAdmin.setProfileImage(admin.getProfileImage());
            }
            userRepository.save(existingAdmin);
        }
    }

    public void saveAdmin(User admin, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            admin.setProfileImage(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        }
        this.saveAdmin(admin);
    }

    //////////////////////////////////////////
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findByRolesContaining("USER");
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getAllUsersBanned() {
        return toDTOs(userRepository.findByBannedTrue());
    }

    public List<UserDTO> getUsersWithReportedReviews() {
        return toDTOs(userRepository.findUsersWithReportedReviews());
    }

    public Page<UserDTO> getUsersPaginated(int page, int size) {
        return userRepository.findByRolesNotContaining("ADMIN", PageRequest.of(page, size)).map(userMapper::toDTO);
    }

    public Page<UserDTO> getUsersWithReportedReviewsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findUsersWithReportedReviews(pageable).map(userMapper::toDTO);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getAdminUsersCount() {
        return userRepository.findByRolesContaining("ADMIN").size();
    }

    public long getRegularUsersCount() {
        return userRepository.findByRolesNotContaining("ADMIN", PageRequest.of(0, Integer.MAX_VALUE))
                .getTotalElements();
    }

    public UserDTO toDTO(User user) {
        return userMapper.toDTO(user);
    }

    public User toDomain(UserDTO userDTO) {
        return userMapper.toDomain(userDTO);
    }

    private List<UserDTO> toDTOs(List<User> users) {
        return userMapper.toDTOs(users);
    }

    // ------------------------------ Register user ------------------------------

    public void validateNewUser(NewUserDTO newUserDTO, BindingResult result) {
        if (newUserDTO.name().isEmpty()) {
            result.rejectValue("name", "error.userDTO", "El nombre no puede estar vacío");
        }
        if (newUserDTO.email().isEmpty()) {
            result.rejectValue("email", "error.userDTO", "El correo electrónico no puede estar vacío");
        }
        if (newUserDTO.encodedPassword().isEmpty()) {
            result.rejectValue("encodedPassword", "error.userDTO", "La contraseña no puede estar vacía");
        }
        if (newUserDTO.confirmPassword().isEmpty()) {
            result.rejectValue("confirmPassword", "error.userDTO",
                    "La confirmación de la contraseña no puede estar vacía");
        }
        if (!newUserDTO.encodedPassword().equals(newUserDTO.confirmPassword())) {
            result.rejectValue("encodedPassword", "error.userDTO", "Las contraseñas no coinciden");
        }
        if (newUserDTO.encodedPassword().length() < 6) {
            result.rejectValue("encodedPassword", "error.userDTO", "La contraseña debe tener al menos 6 caracteres");
        }
        if (userRepository.findByEmail(newUserDTO.email()) != null) {
            result.rejectValue("email", "error.userDTO", "Este correo ya está registrado");
        }
    }

    public UserDTO registerUser(NewUserDTO newUserDTO, String encodedPassword) {
        User user = new User();
        user.setName(newUserDTO.name());
        user.setSurname(newUserDTO.surname());
        user.setEmail(newUserDTO.email());
        user.setEncodedPassword(encodedPassword);
        //user.setEncodedPassword(passwordEncoder.encode(newUserDTO.encodedPassword()));
        //user.setEncodedPassword(newUserDTO.encodedPassword());
        user.setRoles(List.of("USER"));
        User newUser = userRepository.save(user);

        return toDTO(newUser);
    }

    // ------------------------------ Edit user ------------------------------

    public boolean validateEditUser(EditUserDTO editUserDTO, User user, RedirectAttributes redirectAttributes) {
        boolean hasErrors = false;

        if (editUserDTO.newPassword() != null && !editUserDTO.newPassword().isEmpty()) {
            if (editUserDTO.currentPassword() == null || editUserDTO.currentPassword().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorCurrentPassword", "Debe proporcionar su contraseña actual");
                hasErrors = true;
            //} else if (!passwordEncoder.matches(editUserDTO.currentPassword(), user.getEncodedPassword())) {
            } else if (!editUserDTO.currentPassword().equals(user.getEncodedPassword())) {
                redirectAttributes.addFlashAttribute("errorCurrentPassword", "La contraseña actual es incorrecta");
                hasErrors = true;
            }
            if (!editUserDTO.newPassword().equals(editUserDTO.confirmPassword())) {
                redirectAttributes.addFlashAttribute("errorNewPassword", "Las contraseñas no coinciden");
                hasErrors = true;
            }
        }

        if (editUserDTO.email() != null && !editUserDTO.email().isEmpty()) {
            if (!editUserDTO.email().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                redirectAttributes.addFlashAttribute("errorEmail", "El email ingresado no es válido");
                hasErrors = true;
            }
        }

        if (hasErrors) {
            redirectAttributes.addFlashAttribute("editUserDTO", editUserDTO);
        }

        return hasErrors;
    }

    public UserDTO updateUserFromDTO(User user, EditUserDTO editProfileDTO, String encodedPassword) {
        if (editProfileDTO.name() != null && !editProfileDTO.name().isEmpty()) {
            user.setName(editProfileDTO.name());
        }
        if (editProfileDTO.surname() != null && !editProfileDTO.surname().isEmpty()) {
            user.setSurname(editProfileDTO.surname());
        }
        if (editProfileDTO.email() != null && !editProfileDTO.email().isEmpty()) {
            user.setEmail(editProfileDTO.email());
        }
        if (editProfileDTO.address() != null && !editProfileDTO.address().isEmpty()) {
            user.setAddress(editProfileDTO.address());
        }
        if (editProfileDTO.newPassword() != null && !editProfileDTO.newPassword().isEmpty()) {
            user.setEncodedPassword(encodedPassword);
            //user.setEncodedPassword(passwordEncoder.encode(editProfileDTO.newPassword()));
            //user.setEncodedPassword(editProfileDTO.newPassword());

        }


        /*if (editProfileDTO.newImage() != null && !editProfileDTO.newImage().isEmpty()) {
            try {
                Blob imageBlob = BlobProxy.generateProxy(editProfileDTO.newImage().getInputStream(),
                        editProfileDTO.newImage().getSize());
                user.setProfileImage(imageBlob);
            } catch (IOException e) {
                throw new RuntimeException("Error al procesar la imagen", e);
            }
        }
            */

        userRepository.save(user);

        return toDTO(user);
    }

    public UserDTO banUser(Long id) {
        UserDTO userConsult = findById(id);
        User user = toDomain(userConsult);
        User oldUser = findByEmail(user.getEmail());
        user.setImageUrl(oldUser.getImageUrl());
        user.setEncodedPassword(oldUser.getEncodedPassword());
        user.setProfileImage(oldUser.getProfileImage());
        user.setBanned(true);
        user.getReviews().clear();
        saveUser(user);
        
        return toDTO(user);
    }

    public UserDTO unbanUser(Long id) {
        UserDTO userConsult = findById(id);
        User user = toDomain(userConsult);
        User oldUser = findByEmail(user.getEmail());
        user.setImageUrl(oldUser.getImageUrl());
        user.setEncodedPassword(oldUser.getEncodedPassword());
        user.setProfileImage(oldUser.getProfileImage());
        user.setBanned(false);
        saveUser(user);
        
        return toDTO(user);
    }

    public void createUserImage(long id, URI location, InputStream inputStream, long size) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        location = fromCurrentRequest().build().toUri();

        String newLocationStr = location.toString().replace("/user/", "/users/");

        URI newLocation = URI.create(newLocationStr);

        user.setImageUrl(newLocation.toString());
        user.setProfileImage(BlobProxy.generateProxy(inputStream, size));

        userRepository.save(user);
    }


    public Resource getUserImage(long id) throws SQLException {

		User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

		if (user.getProfileImage() != null) {
			return new InputStreamResource(user.getProfileImage().getBinaryStream());
		} else {
			throw new NoSuchElementException();
		}
	}

    public void replaceUserImage(long id, InputStream inputStream, long size) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if(user.getProfileImage() == null){
            throw new NoSuchElementException();
        }
        user.setProfileImage(BlobProxy.generateProxy(inputStream, size));
        userRepository.save(user);
    }


    public void deleteUserImage(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

		if (user.getImageUrl() == null) {
			throw new NoSuchElementException();
		}

		user.setProfileImage(null);
        user.setImageUrl(null);

	    userRepository.save(user);
    }


    

}