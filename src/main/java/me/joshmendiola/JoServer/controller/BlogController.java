package me.joshmendiola.JoServer.controller;

import me.joshmendiola.JoServer.model.Blog;
import me.joshmendiola.JoServer.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
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
}