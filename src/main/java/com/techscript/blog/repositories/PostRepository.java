package com.techscript.blog.repositories;

import com.techscript.blog.entities.Category;
import com.techscript.blog.entities.Post;
import com.techscript.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByUser(User user);

    List<Post> findByCategory(Category category);

    List<Post> findByTitleContaining(String title);
}