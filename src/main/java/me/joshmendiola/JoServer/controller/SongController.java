package me.joshmendiola.JoServer.controller;

import me.joshmendiola.JoServer.model.Song;
import me.joshmendiola.JoServer.repository.SongRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
public class SongController
{
    @Autowired
    private SongRepository repository;
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/songs")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Song> getAllSongs()
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

    @PostMapping(value = "/song", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Song addSong(@RequestPart("cover") @NotNull MultipartFile cover,
                        @RequestPart("song") @NotNull Song songData,
                        @RequestPart("audio") @NotNull MultipartFile audio) throws IOException
    {
        byte[] fileBytes = cover.getBytes();

        String uploadsFolder = resourceLoader.getResource("classpath:uploads/").getFile().getAbsolutePath();

        Path coverFilePath = Paths.get(uploadsFolder, cover.getOriginalFilename() + UUID.randomUUID());
        Path audioFilePath = Paths.get(uploadsFolder, audio.getOriginalFilename() + UUID.randomUUID());

        Files.write(coverFilePath, fileBytes);

        UUID uuid = UUID.randomUUID();
        Song song = new Song();

        System.out.println(songData.getAuthor());

        song.setSong_id(uuid);
        song.setTitle(songData.getTitle());
        song.setDate(songData.getDate());
        song.setAbout(songData.getAbout());
        song.setAuthor(songData.getAuthor());
        song.setCover(coverFilePath.toString());
        song.setAudio(audioFilePath.toString());

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
        song.setDate(newSong.getDate());
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
