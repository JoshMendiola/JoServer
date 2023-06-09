package me.joshmendiola.JoServer.controller;

import lombok.RequiredArgsConstructor;
import me.joshmendiola.JoServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController
{
    @Autowired
    private UserRepository repository;

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteUser(@PathVariable UUID id)
    {
        if(repository.findById(id).isEmpty())
        {
            throw new NullPointerException("ERROR: No songs with that ID found !");
        }
        repository.deleteById(id);
    }
}
