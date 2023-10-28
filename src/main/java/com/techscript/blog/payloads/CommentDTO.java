package com.techscript.blog.payloads;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentDTO {

    private Integer commentId;
    private String content;
}