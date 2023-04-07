package me.joshmendiola.JoServer.repository;

import me.joshmendiola.JoServer.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SongRepository extends JpaRepository<Song, UUID>
{
    List<Song> getSongsByAuthor(String author);
}
