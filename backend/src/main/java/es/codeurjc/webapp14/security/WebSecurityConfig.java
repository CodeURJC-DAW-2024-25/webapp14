
package es.codeurjc.webapp14.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	RepositoryUserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    
        http.authenticationProvider(authenticationProvider());
    
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/index/**", "/register", "/login", "/logout", "/register-success").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/vendor/**", "/videos/**").permitAll()
                .requestMatchers("/login_register/**", "/access-error", "/no-page-error/**").permitAll()
                .requestMatchers("/image/**").permitAll()
                .requestMatchers("/error", "/error/**").permitAll()
                .requestMatchers("/cart/**", "/orders/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .successHandler(new UserAuthHandler())
                .failureUrl("/access-error")
                .permitAll()
            )
            .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/index")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        );
    
        return http.build();
    }
    
}