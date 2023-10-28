package com.techscript.blog.services.impl;

import com.techscript.blog.entities.Comment;
import com.techscript.blog.entities.Post;
import com.techscript.blog.exceptions.ResourceNotFoundException;
import com.techscript.blog.payloads.CommentDTO;
import com.techscript.blog.repositories.CommentRepository;
import com.techscript.blog.repositories.PostRepository;
import com.techscript.blog.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDTO createComment(CommentDTO commentDTO, Integer postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "PostId", postId));

        Comment comment = this.modelMapper.map(commentDTO, Comment.class);
        comment.setPost(post);

        Comment savedComment = this.commentRepository.save(comment);

        return this.modelMapper.map(savedComment,CommentDTO.class);
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = this.commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "CommentId", commentId));
        this.commentRepository.delete(comment);
    }
}