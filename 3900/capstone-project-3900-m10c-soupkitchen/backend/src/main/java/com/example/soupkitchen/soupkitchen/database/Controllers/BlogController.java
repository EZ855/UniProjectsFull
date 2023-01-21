package com.example.soupkitchen.soupkitchen.database.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.soupkitchen.soupkitchen.database.Database.Blog;
import com.example.soupkitchen.soupkitchen.database.Repositories.blogsRepository;

import java.util.List;
import java.util.Map;

@RestController
public class BlogController {

    @Autowired
    blogsRepository blogRepository;

    @GetMapping("/blog")
    public List<Blog> index(){
        return blogRepository.findAll();
    }

    @GetMapping("/blog/{id}")
    public Blog show(@PathVariable String id){
        int blogId = Integer.parseInt(id);
        return blogRepository.findById(blogId).get();
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/blog/search", consumes = {"application/json"})
    public List<Blog> search(@RequestBody Map<String, String> body){
        String searchTerm = body.get("text");
        return blogRepository.findByTitleContainingOrContentContaining(searchTerm, searchTerm);
    }

    //@CrossOrigin(origins = "*")
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/blog", consumes = {"application/json"})
    public Blog create(@RequestBody Map<String, String> body){
        String title = body.get("title");
        String content = body.get("content");
        return blogRepository.save(new Blog(title, content));
    }

    @PutMapping("/blog/{id}")
    public Blog update(@PathVariable String id, @RequestBody Map<String, String> body){
        int blogId = Integer.parseInt(id);
        // getting blog
        Blog blog = blogRepository.findById(blogId).get();
        blog.setTitle(body.get("title"));
        blog.setContent(body.get("content"));
        return blogRepository.save(blog);
    }

    @DeleteMapping("blog/{id}")
    public boolean delete(@PathVariable String id){
        int blogId = Integer.parseInt(id);
        blogRepository.deleteById(blogId);
        return true;
    }

}