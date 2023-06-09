package me.joshmendiola.JoServer.controller;

import lombok.RequiredArgsConstructor;
import me.joshmendiola.JoServer.service.configs.AuthenticationService;
import me.joshmendiola.JoServer.service.request.AuthenticationRequest;
import me.joshmendiola.JoServer.service.request.RegisterRequest;
import me.joshmendiola.JoServer.service.response.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/* this class generally handles any attempts to authenticate,
sending the request to the correct functions of the service and returning 200 ok if successful
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController
{

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
    {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
