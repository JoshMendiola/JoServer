package me.joshmendiola.JoServer.controller;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import me.joshmendiola.JoServer.model.Song;
import me.joshmendiola.JoServer.repository.SongRepository;
import me.joshmendiola.JoServer.service.configs.JschConfig;
import me.joshmendiola.JoServer.service.utils.Utility;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SongController
{
    @Autowired
    private SongRepository repository;
    @Autowired
    private ResourceLoader resourceLoader;

    JschConfig jsch = new JschConfig();

    @GetMapping("/songs")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Song> getAllSongs()
    {
        return repository.findAll();
    }

    @GetMapping("/song/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Song getSongByID(@PathVariable UUID id) throws IOException
    {
        Optional<Song> songOptional = repository.findById(id);

        if(songOptional.isEmpty())
        {
            throw new NullPointerException("No Blog with that ID found !");
        }

//        Song song = songOptional.get();
//
//        byte[] mp3Bytes = Files.readAllBytes(Paths.get("/path/to/mp3file.mp3"));
//        String mp3Base64 = Base64.encodeBase64String(mp3Bytes);
//        byte[] jpgBytes = Files.readAllBytes(Paths.get("/path/to/jpgfile.jpg"));
//        String jpgBase64 = Base64.encodeBase64String(jpgBytes);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("song", song);
//        response.put("mp3file", mp3Base64);
//        response.put("jpgfile", jpgBase64);
        return songOptional.get();
    }

    @PostMapping(value = "/song", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public Song addSong(@RequestPart("cover") @NotNull MultipartFile cover,
                        @RequestPart("song") @NotNull Song songData,
                        @RequestPart("audio") @NotNull MultipartFile audio) throws IOException, JSchException, SftpException
    {
        ChannelSftp channelSftp = jsch.setupJsch();
        channelSftp.connect();

        String coverFileName = Utility.generateUniqueFilename();
        String audioFileName = Utility.generateUniqueFilename();
        String coverExtension = "." + StringUtils.getFilenameExtension(cover.getOriginalFilename());
        String audioExtension = "." + StringUtils.getFilenameExtension(audio.getOriginalFilename());
        String coverDir = "/Users/joshuamendiola/JohmenBlogFiles/Covers/"  + coverFileName + coverExtension;
        String audioDir = "/Users/joshuamendiola/JohmenBlogFiles/Audios/"  + audioFileName + audioExtension;

        byte[] coverFileBytes = cover.getBytes();
        byte[] audioFileBytes = audio.getBytes();
        ByteArrayInputStream coverStream = new ByteArrayInputStream(coverFileBytes);
        ByteArrayInputStream audioStream = new ByteArrayInputStream(audioFileBytes);


        channelSftp.put(coverStream, coverDir);
        channelSftp.put(audioStream, audioDir);

        UUID uuid = UUID.randomUUID();
        Song song = new Song();

        System.out.println(songData.getAuthor());

        song.setSong_id(uuid);
        song.setTitle(songData.getTitle());
        song.setDate(songData.getDate());
        song.setAbout(songData.getAbout());
        song.setAuthor(songData.getAuthor());
        song.setCover(coverFileName);
        song.setAudio(audioFileName);

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
        Song song = repository.getReferenceById(id);
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
