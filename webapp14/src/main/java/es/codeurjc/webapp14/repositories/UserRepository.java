package es.codeurjc.webapp14.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.codeurjc.webapp14.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    // User findByRole(User.Role role);

    Optional<User> findById(Long id);

    List<User> findByBannedTrue();

    @Query("SELECT u FROM User u JOIN u.reviews r WHERE r.reported = true")
    List<User> findUsersWithReportedReviews();

    @Query("SELECT u FROM User u JOIN u.reviews r WHERE r.reported = true")
    Page<User> findUsersWithReportedReviews(Pageable pageable);

    Optional<User> findByName(String username);

    List<User> findByRolesContaining(String role);

    Page<User> findByRolesNotContaining(String string, PageRequest of);
}
