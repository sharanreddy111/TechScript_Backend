package com.techscript.blog.services.impl;

import com.techscript.blog.entities.Category;
import com.techscript.blog.entities.Post;
import com.techscript.blog.entities.User;
import com.techscript.blog.exceptions.ResourceNotFoundException;
import com.techscript.blog.payloads.PostDTO;
import com.techscript.blog.payloads.PostResponse;
import com.techscript.blog.repositories.CategoryRepository;
import com.techscript.blog.repositories.PostRepository;
import com.techscript.blog.repositories.UserRepository;
import com.techscript.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PostDTO createPost(PostDTO postDTO, Integer userId, Integer categoryId) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","User Id", userId));

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "Category Id", categoryId));

        Post post = this.dtoToEntity(postDTO);

        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);

        Post newPost = this.postRepository.save(post);

        return this.entityToDto(newPost);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Integer postId) {

        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "PostId", postId));

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setImageName(postDTO.getImageName());

        Post updatedPost = this.postRepository.save(post);

        return this.entityToDto(updatedPost);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "PostId", postId));
        this.postRepository.delete(post);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);

        Page<Post> pagePost = this.postRepository.findAll(pageable);
        List<Post> allPosts = pagePost.getContent();
        List<PostDTO> allPostsDTO = allPosts.stream().map(this::entityToDto).toList();

        PostResponse postResponse = new PostResponse();
         postResponse.setContent(allPostsDTO);
         postResponse.setPageNumber(pagePost.getNumber());
         postResponse.setPageSize(pagePost.getSize());
         postResponse.setTotalElements((int) pagePost.getTotalElements());
         postResponse.setTotalPages(pagePost.getTotalPages());
         postResponse.setLastPage(pagePost.isLast());

        return postResponse;
    }

    @Override
    public PostDTO getPostById(Integer postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "PostId", postId));
        return this.entityToDto(post);
    }

    @Override
    public List<PostDTO> getPostsByCategory(Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        List<Post> posts = this.postRepository.findByCategory(category);

        return posts.stream().map(this::entityToDto).toList();
    }

    @Override
    public List<PostDTO> getPostsByUser(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "UserId", userId));

        List<Post> posts = this.postRepository.findByUser(user);

        return posts.stream().map(this::entityToDto).toList();
    }

    @Override
    public List<PostDTO> searchPostsByTitle(String keyword) {
        List<Post> byTitleContaining = this.postRepository.findByTitleContaining(keyword);
        return byTitleContaining.stream().map(this::entityToDto).toList();
    }

    public Post dtoToEntity(PostDTO postDTO){
        return this.modelMapper.map(postDTO,Post.class);
    }

    public PostDTO entityToDto(Post post){
        return this.modelMapper.map(post,PostDTO.class);
    }
}