package me.joshmendiola.JoServer.controller;

import me.joshmendiola.JoServer.model.Blog;
import me.joshmendiola.JoServer.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogControllerTests
{
    @InjectMocks
    private BlogController blogController;

    @Mock
    private BlogRepository blogRepository;

    private Blog testBlog;
    private UUID testUUID;

    @BeforeEach
    public void setUp() {
        testUUID = UUID.randomUUID();
        long time = System.currentTimeMillis();
        Date testDate = new Date(time);
        testBlog = new Blog();
        testBlog.setBlog_id(testUUID);
        testBlog.setTitle("Test Title");
        testBlog.setAuthor("Test Author");
        testBlog.setBody("Test Body");
        testBlog.setDate(testDate);
    }

    @Test
    public void testGetAllBlogs() {
        List<Blog> blogs = new ArrayList<>();
        blogs.add(testBlog);
        when(blogRepository.findAll()).thenReturn(blogs);

        List<Blog> result = blogController.getALlBlogs();

        assertEquals(blogs, result);
        verify(blogRepository, times(1)).findAll();
    }

    // ...

    @Test
    public void testGetBlogById() {
        when(blogRepository.findById(testUUID)).thenReturn(Optional.of(testBlog));

        Blog result = blogController.getBlogByID(testUUID);

        assertEquals(testBlog, result);
        verify(blogRepository, times(1)).findById(testUUID);
    }

    @Test
    public void testGetBlogByIdNotFound() {
        when(blogRepository.findById(testUUID)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> blogController.getBlogByID(testUUID));
        verify(blogRepository, times(1)).findById(testUUID);
    }

    @Test
    public void testAddBlog() {
        when(blogRepository.existsById(any())).thenReturn(false);
        when(blogRepository.save(any())).thenReturn(testBlog);

        Blog result = blogController.addBlog(testBlog);

        assertEquals(testBlog, result);
        verify(blogRepository, times(1)).existsById(any());
        verify(blogRepository, times(1)).save(any());
    }

    @Test
    public void testAddBlogWithDuplicateId() {
        when(blogRepository.existsById(any())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> blogController.addBlog(testBlog));
        verify(blogRepository, times(1)).existsById(any());
    }

    @Test
    public void testUpdateBlog() {
        Blog updatedBlog = new Blog();
        updatedBlog.setAuthor("Updated Author");
        updatedBlog.setTitle("Updated Title");
        updatedBlog.setBody("Updated Body");
        long time = System.currentTimeMillis() + 100000;
        Date updatedDate = new Date(time);
        updatedBlog.setDate(updatedDate);

        when(blogRepository.getReferenceById(testUUID)).thenReturn(testBlog);

        blogController.updateBlog(updatedBlog, testUUID);

        assertEquals(updatedBlog.getAuthor(), testBlog.getAuthor());
        assertEquals(updatedBlog.getTitle(), testBlog.getTitle());
        assertEquals(updatedBlog.getBody(), testBlog.getBody());
        assertEquals(updatedBlog.getDate(), testBlog.getDate());
        verify(blogRepository, times(1)).getReferenceById(testUUID);
        verify(blogRepository, times(1)).save(testBlog);
    }

    @Test
    public void testDeleteBlog() {
        when(blogRepository.findById(testUUID)).thenReturn(Optional.of(testBlog));

        blogController.deleteBlog(testUUID);

        verify(blogRepository, times(1)).findById(testUUID);
        verify(blogRepository, times(1)).deleteById(testUUID);
    }

    @Test
    public void testDeleteBlogNotFound() {
        when(blogRepository.findById(testUUID)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> blogController.deleteBlog(testUUID));
        verify(blogRepository, times(1)).findById(testUUID);
    }


}
