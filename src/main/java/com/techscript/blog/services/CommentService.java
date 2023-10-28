package com.techscript.blog.services;

import com.techscript.blog.payloads.CommentDTO;

public interface CommentService {

    CommentDTO createComment(CommentDTO commentDTO, Integer postId);
    void deleteComment(Integer commentId);
}
