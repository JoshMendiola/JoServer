package me.joshmendiola.JoServer.repository;

import me.joshmendiola.JoServer.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SongRepository extends JpaRepository<Song, UUID>
{
    List<Song> getSongsByArtist();
}
