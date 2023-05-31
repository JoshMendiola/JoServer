package me.joshmendiola.JoServer.service.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest
{
    private String username;
    private String password;
}
