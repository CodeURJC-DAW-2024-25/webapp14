
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
				
						// PUBLIC PAGES
						.requestMatchers("/index/**").permitAll()
						.requestMatchers("/css/**", "/js/**", "/images/**", "/vendor/**", "/videos/**").permitAll()
						.requestMatchers("/register").permitAll()
						.requestMatchers("/login").permitAll()
						.requestMatchers("/logout").permitAll()
						.requestMatchers("/register").permitAll()
						.requestMatchers("/register-success").permitAll()
						.requestMatchers("/login_register/**").permitAll()
						.requestMatchers("/index/*/*/edit").permitAll()
						.requestMatchers("/access-error").permitAll()
						.requestMatchers("/cart").permitAll()
						.requestMatchers("/orders/**").permitAll()
						.requestMatchers("/image/**").permitAll()
						
						
						// PRIVATE PAGES

						.requestMatchers("/admin/**").permitAll());

						

		return http.build();
	}
}