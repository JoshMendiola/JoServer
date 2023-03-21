package me.joshmendiola.JoServer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "Blogs")
public class Blog
{
    @Id
    @GeneratedValue
    @UuidGenerator
    @Getter
    @Setter
    private UUID blog_id;

    @Getter
    @Setter
    private String author;

    @Getter
    @Setter
    private Date date;

    @Getter
    @Setter
    private String body;

    @Getter
    @Setter
    private String title;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Blog blog = (Blog) o;
        return blog_id != null && Objects.equals(blog_id, blog.blog_id);
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
