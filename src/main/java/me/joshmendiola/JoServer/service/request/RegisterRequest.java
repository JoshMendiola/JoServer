package me.joshmendiola.JoServer.service.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest
{
    private String username;
    private String password;
    private String email;
}
