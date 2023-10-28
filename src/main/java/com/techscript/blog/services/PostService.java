package com.techscript.blog.services;

import com.techscript.blog.entities.Post;
import com.techscript.blog.payloads.PostDTO;
import com.techscript.blog.payloads.PostResponse;

import java.util.List;

public interface PostService {

    PostDTO createPost(PostDTO postDTO, Integer userId, Integer categoryId);

    PostDTO updatePost(PostDTO postDTO, Integer postId);

    void deletePost(Integer postId);

    PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    PostDTO getPostById(Integer postId);

    List<PostDTO> getPostsByCategory(Integer categoryId);

    List<PostDTO> getPostsByUser(Integer userId);

    List<PostDTO> searchPostsByTitle(String keyword);
}