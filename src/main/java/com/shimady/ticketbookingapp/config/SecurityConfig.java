package com.shimady.ticketbookingapp.config;

import com.shimady.ticketbookingapp.CustomAuthenticationFailureHandler;
import com.shimady.ticketbookingapp.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class SecurityConfig {

    private final String[] AUTH_WHITELIST_PATHS = new String[]{
            "/user/signup",
            "/tickets/*",
            "/seats",
            "/token",
            "/token/update",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return (username) -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) //disable csrf for postman testing
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(AUTH_WHITELIST_PATHS).permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/user/login").permitAll()
                        .defaultSuccessUrl("/user")
                        .failureHandler(authenticationFailureHandler()))
                .logout(logout -> logout
                        .logoutUrl("/user/logout").permitAll())
                .build();
    }
}
