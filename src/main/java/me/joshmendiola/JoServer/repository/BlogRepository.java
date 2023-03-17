package me.joshmendiola.JoServer.repository;

import me.joshmendiola.JoServer.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface BlogRepository extends JpaRepository<Blog, UUID>
{
    List<Blog > getBlogsByDate(Date date);
}
