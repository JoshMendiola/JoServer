package me.joshmendiola.JoServer.controller;

import me.joshmendiola.JoServer.model.Blog;
import me.joshmendiola.JoServer.model.Song;
import me.joshmendiola.JoServer.repository.SongRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
public class MusicController
{
    @Autowired
    private SongRepository repository;

    @GetMapping("/songs")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Song> getALlSongs()
    {
        return repository.findAll();
    }

    @GetMapping("/song/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Song getSongByID(@PathVariable UUID id)
    {
        Optional<Song> song = repository.findById(id);
        if(song.isEmpty())
        {
            throw new NullPointerException("No Blog with that ID found !");
        }
        else
        {
            return song.get();
        }
    }

    @PostMapping("/song")
    @ResponseStatus(HttpStatus.CREATED)
    public Song addSong(@RequestBody @NotNull Song song)
    {
        UUID uuid = UUID.randomUUID();
        song.setSong_id(uuid);

        if(repository.existsById(song.getSong_id()))
        {
            throw new IllegalArgumentException("Error: A song with that ID already exists in the database!");
        }
        return repository.save(song);
    }

    @PutMapping("/song/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSong(@RequestBody @NotNull Song newSong, @PathVariable UUID id)
    {
        Song song = new Song();
        song.setAuthor(newSong.getAuthor());
        song.setAbout(newSong.getAbout());
        song.setTitle(newSong.getTitle());
        song.setCover(newSong.getCover());
        song.setAudio(newSong.getAudio());
        repository.save(song);
    }

    @DeleteMapping("/song/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSong(@PathVariable UUID id)
    {
        if(repository.findById(id).isEmpty())
        {
            throw new NullPointerException("ERROR: No songs with that ID found !");
        }
        repository.deleteById(id);
    }
}
