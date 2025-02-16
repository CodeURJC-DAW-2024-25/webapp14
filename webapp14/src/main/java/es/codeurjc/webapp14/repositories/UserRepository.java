package es.codeurjc.webapp14.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.webapp14.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
