package com.techscript.blog.controllers;

import com.techscript.blog.config.AppConstants;
import com.techscript.blog.payloads.ApiResponse;
import com.techscript.blog.payloads.PostDTO;
import com.techscript.blog.payloads.PostResponse;
import com.techscript.blog.services.FileService;
import com.techscript.blog.services.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class PostController {

    @Value("${project.image}")
    private String path;

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO,
                                              @PathVariable Integer userId,
                                              @PathVariable Integer categoryId){
        PostDTO createdPost = this.postService.createPost(postDTO, userId, categoryId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable Integer userId){
        List<PostDTO> postsByUser = this.postService.getPostsByUser(userId);
        return new ResponseEntity<>(postsByUser,HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}/posts")
    private ResponseEntity<List<PostDTO>> getPostsByCategory(@PathVariable Integer categoryId){
        List<PostDTO> postsByCategory = this.postService.getPostsByCategory(categoryId);
        return new ResponseEntity<>(postsByCategory,HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR, required = false) String sortDir){

        PostResponse allPosts = this.postService.getAllPosts(pageNumber, pageSize, sortBy,sortDir);
        return new ResponseEntity<>(allPosts,HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Integer postId){
        PostDTO postById = this.postService.getPostById(postId);
        return new ResponseEntity<>(postById,HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}")
    public ApiResponse deletePost(@PathVariable Integer postId){
        this.postService.deletePost(postId);
        return new ApiResponse("Post deleted successfully",true);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable Integer postId){
        PostDTO updatedPost = this.postService.updatePost(postDTO, postId);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @GetMapping("/posts/search/{keywords}")
    public ResponseEntity<List<PostDTO>> searchPostByTitle(@PathVariable String keywords){
        List<PostDTO> searchResult = this.postService.searchPostsByTitle(keywords);
        return new ResponseEntity<>(searchResult,HttpStatus.OK);
    }

    //image upload
    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDTO> uploadPostImage(
            @RequestParam MultipartFile image,
            @PathVariable Integer postId) throws IOException {
        PostDTO postDTO = this.postService.getPostById(postId);

        String fileName = this.fileService.uploadImage(path, image);
        postDTO.setImageName(fileName);
        PostDTO updatedPost = this.postService.updatePost(postDTO, postId);

        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @GetMapping(value = "/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable String imageName,
            HttpServletResponse response) throws IOException{

        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}