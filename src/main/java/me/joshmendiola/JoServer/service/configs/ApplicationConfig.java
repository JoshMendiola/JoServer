package me.joshmendiola.JoServer.service.configs;

import lombok.AllArgsConstructor;
import me.joshmendiola.JoServer.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@AllArgsConstructor
public class ApplicationConfig
{
    private final UserRepository repository;
    @Bean
    public UserDetailsService userDetailsService()
    {
        return username -> repository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found"));
    }
}
