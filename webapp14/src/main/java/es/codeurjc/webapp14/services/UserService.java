package es.codeurjc.webapp14.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public List<User> getAllUsers() {
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

    public List<User> getAllUsersBanned() {
        return userRepository.findByBannedTrue();
    }

    public List<User> getUsersWithReportedReviews() {
        return userRepository.findUsersWithReportedReviews();
    }

    public Page<User> getUsersPaginated(int page, int size) {
        return userRepository.findByRolesNotContaining("ADMIN", PageRequest.of(page, size));
    }

    public Page<User> getUsersWithReportedReviewsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findUsersWithReportedReviews(pageable);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
