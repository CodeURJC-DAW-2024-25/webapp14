package es.codeurjc.webapp14.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.codeurjc.webapp14.security.jwt.JwtRequestFilter;
import es.codeurjc.webapp14.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .securityMatcher("/api/**")
                .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

        http
                .authorizeHttpRequests(authorize -> authorize
                        // PUBLIC ENDPOINTS
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                        .requestMatchers("/api/v1/category/**").permitAll()
                        .requestMatchers("/api/v1/search/**").permitAll()
                        .requestMatchers("/api/v1/image/**").permitAll()

                        // PRIVATE ENDPOINTS

                        .requestMatchers(HttpMethod.POST, "/api/v1/products/{productId}/reviews").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/products/{id}/reviews/{reviewId}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/{id}/reviews/{reviewId}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/{id}/reviews/{reviewId}").hasRole("USER")

                        .requestMatchers(HttpMethod.POST, "/api/v1/products/image/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/image/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/products/image/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/image/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/cart/**").hasRole("USER")
                        .requestMatchers("/api/v1/orders/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/reviews/**").authenticated()
                        .requestMatchers("/api/v1/users/**").authenticated()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/v3/api-docs.yaml").permitAll()
                        .requestMatchers("/api/v1/charts").hasRole("ADMIN")

                        .requestMatchers("/api/v1/**").permitAll()

                        .anyRequest().authenticated());

        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Enable CORS for Angular frontend
        http.cors(cors -> cors.configurationSource(request -> {
            var corsConfig = new org.springframework.web.cors.CorsConfiguration();
            corsConfig.setAllowedOrigins(List.of("http://localhost:4200")); // Angular dev server
            corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            corsConfig.setAllowedHeaders(List.of("*"));
            corsConfig.setAllowCredentials(true); // Necesario si usas JWT con cookies
            return corsConfig;
        }));

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT Token filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/cart/**", "/orders/**").authenticated()
                        .requestMatchers("/user_registered/users_profile", "/editProfile").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .successHandler(new UserAuthHandler())
                        .failureUrl("/access-error")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-error"));

        return http.build();
    }
}
