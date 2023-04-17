package me.joshmendiola.JoServer.repository;

import me.joshmendiola.JoServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>
{

}
