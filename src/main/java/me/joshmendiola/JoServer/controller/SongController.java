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
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    public ResponseEntity<Song> getSongByID(@PathVariable UUID id) throws JSchException, SftpException, IOException {
        ChannelSftp channelSftp = jsch.setupJsch();
        channelSftp.connect();
        try {
            Song song = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid song ID: " + id));
            String coverFileName = song.getCover();
            String audioFileName = song.getAudio();
            String coverDir = "/Users/joshuamendiola/JohmenBlogFiles/Covers/" + coverFileName;
            String audioDir = "/Users/joshuamendiola/JohmenBlogFiles/Audios/" + audioFileName;
            System.out.println(coverFileName);
            System.out.println(audioFileName);
            ByteArrayOutputStream coverByteArrayOutputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream audioByteArrayOutputStream = new ByteArrayOutputStream();

            try {
                channelSftp.get(coverDir, coverByteArrayOutputStream);
            } catch (SftpException e) {
                coverByteArrayOutputStream.close();
                throw new RuntimeException("Failed to get cover file for song with ID: " + id, e);
            }
            try {
                channelSftp.get(audioDir, audioByteArrayOutputStream);
            } catch (SftpException e) {
                coverByteArrayOutputStream.close();
                audioByteArrayOutputStream.close();
                throw new RuntimeException("Failed to get audio file for song with ID: " + id, e);
            }
            byte[] coverBytes = coverByteArrayOutputStream.toByteArray();
            byte[] audioBytes = audioByteArrayOutputStream.toByteArray();
            Song songWithFiles = new Song();
            songWithFiles.setSong_id(song.getSong_id());
            songWithFiles.setTitle(song.getTitle());
            songWithFiles.setDate(song.getDate());
            songWithFiles.setAbout(song.getAbout());
            songWithFiles.setAuthor(song.getAuthor());
            songWithFiles.setCover(Arrays.toString(coverBytes));
            songWithFiles.setAudio(Arrays.toString(audioBytes));
            return ResponseEntity.ok().body(songWithFiles);
        } finally {
            channelSftp.disconnect();
        }
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

        System.out.println(coverFileName + coverExtension);
        System.out.println(audioFileName + audioExtension);

        song.setSong_id(uuid);
        song.setTitle(songData.getTitle());
        song.setDate(songData.getDate());
        song.setAbout(songData.getAbout());
        song.setAuthor(songData.getAuthor());
        song.setCover(coverFileName + coverExtension);
        song.setAudio(audioFileName + audioExtension);

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
