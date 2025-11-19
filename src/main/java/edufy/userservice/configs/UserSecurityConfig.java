package edufy.userservice.configs;

import edufy.userservice.exceptions.ResourceNotFoundException;
import edufy.userservice.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails joey = User.builder()
                .username("joey")
                .password(passwordEncoder.encode("joey"))
                .roles("ADMIN")
                .build();

        UserDetails ross = User.builder()
                .username("ross")
                .password(passwordEncoder.encode("ross"))
                .roles("USER")
                .build();

        UserDetails rachel = User.builder()
                .username("rachel")
                .password(passwordEncoder.encode("rachel"))
                .roles("USER")
                .build();

        UserDetails chandler = User.builder()
                .username("chandler")
                .password(passwordEncoder.encode("chandler"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(joey,ross,rachel,chandler);
    }

    @Bean
    public UserDetailsService customUserDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRoles())
                        .build()
                ).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        // Alla endpoints öppna
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
