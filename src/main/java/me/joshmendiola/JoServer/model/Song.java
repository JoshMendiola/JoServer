package me.joshmendiola.JoServer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;
import java.util.UUID;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "Songs")
public class Song
{
    @Id
    @GeneratedValue
    @UuidGenerator
    @Getter
    @Setter
    private UUID song_id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String about;

    @Getter
    @Setter
    private String author;

    @Getter
    @Setter
    private String audio;

    @Getter
    @Setter
    private String cover;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Song song)) return false;
        return getSong_id().equals(song.getSong_id()) && getTitle().equals(song.getTitle()) && Objects.equals(getAbout(), song.getAbout()) && getAuthor().equals(song.getAuthor()) && getAudio().equals(song.getAudio()) && Objects.equals(getCover(), song.getCover());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getSong_id(), getTitle(), getAbout(), getAuthor(), getAudio(), getCover());
    }
}
