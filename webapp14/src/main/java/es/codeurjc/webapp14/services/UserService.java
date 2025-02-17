package es.codeurjc.webapp14.services;

import java.io.IOException;
import java.util.List;
import org.hibernate.engine.jdbc.BlobProxy;
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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getAdmin() {
        return userRepository.findByRole(User.Role.ADMIN);
    }

    public void saveAdmin(User admin) {
        User existingAdmin = getAdmin();
        if (existingAdmin == null) {
            userRepository.save(admin);
        } else {
            existingAdmin.setName(admin.getName());
            existingAdmin.setSurname(admin.getSurname());
            existingAdmin.setEmail(admin.getEmail());
            existingAdmin.setPassword(admin.getPassword());
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

}
