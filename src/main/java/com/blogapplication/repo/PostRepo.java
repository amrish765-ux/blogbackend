package com.blogapplication.repo;

import com.blogapplication.entities.Categories;
import com.blogapplication.entities.Post;
import com.blogapplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Integer> {
    List<Post>findByUser(User user);
    List<Post>findByCategories(Categories categories);
    List<Post>findByTitleContaining(String title);
}
