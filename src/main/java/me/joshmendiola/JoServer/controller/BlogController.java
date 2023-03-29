package me.joshmendiola.JoServer.controller;

import me.joshmendiola.JoServer.model.Blog;
import me.joshmendiola.JoServer.repository.BlogRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
public class BlogController
{
    @Autowired
    private BlogRepository repository;

    @GetMapping("/blogs")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Blog> getALlBlogs()
    {
        return repository.findAll();
    }

    @GetMapping("/blog/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Blog getBlogByID(@PathVariable UUID id)
    {
        Optional<Blog> blog = repository.findById(id);
        if(blog.isEmpty())
        {
            throw new NullPointerException("No Blog with that ID found !");
        }
        else
        {
            return blog.get();
        }
    }

    @PostMapping("/blog")
    @ResponseStatus(HttpStatus.CREATED)
    public Blog addBlog(@RequestBody @NotNull Blog blog)
    {
        UUID uuid = UUID.randomUUID();
        blog.setBlog_id(uuid);

        if(repository.existsById(blog.getBlog_id()))
        {
            throw new IllegalArgumentException("Error: A blog with that ID already exists in the database!");
        }
        return repository.save(blog);
    }

    @PutMapping("/blog/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBlog(@RequestBody @NotNull Blog newBlog, @PathVariable UUID id)
    {
        Blog blog = repository.getReferenceById(id);
        blog.setAuthor(newBlog.getAuthor());
        blog.setTitle(newBlog.getTitle());
        blog.setBody(newBlog.getBody());
        blog.setDate(newBlog.getDate());
        repository.save(blog);
    }

    @DeleteMapping("/blog/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBlog(@PathVariable UUID id)
    {
        if(repository.findById(id).isEmpty())
        {
            throw new NullPointerException("ERROR: No blogs with that ID found !");
        }
        repository.deleteById(id);
    }

}
