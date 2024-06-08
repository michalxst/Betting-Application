package betsapp.air.betsapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class UserRegistrationSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityChain(HttpSecurity http) throws Exception {
        http.csrf().disable().
                authorizeHttpRequests().requestMatchers("/register/**", "/home").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .and().formLogin().loginProcessingUrl("/login")
                .and().formLogin().defaultSuccessUrl("/").permitAll()
                .and().logout().logoutSuccessUrl("/login");
        return http.build();
    }
}
