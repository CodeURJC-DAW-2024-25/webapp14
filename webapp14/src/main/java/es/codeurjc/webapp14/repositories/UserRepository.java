package es.codeurjc.webapp14.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.webapp14.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByRole(User.Role role);

    Optional<User> findById(Long id);

}
