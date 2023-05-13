package me.joshmendiola.JoServer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import me.joshmendiola.JoServer.enums.Role;

import java.util.Objects;
import java.util.UUID;

@Entity
public class User
{
    @Id
    @Getter
    @Setter
    private UUID userId;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private Role role;

    @Getter
    @Setter
    private String bio;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getUserId(), user.getUserId()) && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword()) && getRole() == user.getRole() && Objects.equals(getBio(), user.getBio());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getUserId(), getUsername(), getPassword(), getRole(), getBio());
    }

    @Override
    public String toString()
    {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", bio='" + bio + '\'' +
                '}';
    }
}
