package es.codeurjc.webapp14.services;

import java.util.List;
import org.springframework.stereotype.Service;
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
}
