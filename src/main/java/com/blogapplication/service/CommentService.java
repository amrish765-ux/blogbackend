package com.blogapplication.service;

import com.blogapplication.payload.CommentDto;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {


    CommentDto createComment(CommentDto commentDto,Integer postId);
    void deleteComment(Integer commentId);

}
