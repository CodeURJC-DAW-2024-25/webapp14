package es.codeurjc.webapp14.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepositoryUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public RepositoryUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))


            .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getEncodedPassword())
            .authorities(authorities)
            .build();
    }
}
