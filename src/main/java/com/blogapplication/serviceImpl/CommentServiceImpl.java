package com.blogapplication.serviceImpl;

import com.blogapplication.Exception.ResourceNotFoundException;
import com.blogapplication.entities.Comment;
import com.blogapplication.entities.Post;
import com.blogapplication.payload.CommentDto;
import com.blogapplication.repo.CommentRepo;
import com.blogapplication.repo.PostRepo;
import com.blogapplication.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentRepo commentRepo;

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {
        Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","postId",postId));
        Comment comment = this.modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);
        Comment savedComment=this.commentRepo.save(comment);
        System.out.println("Mapped CommentDto ID: " + savedComment.getId());
        CommentDto cm=this.modelMapper.map(savedComment,CommentDto.class);
        cm.setId(savedComment.getId());

        return cm;
//        return this.modelMapper.map(savedComment,CommentDto.class);
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment=this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment","comment id",commentId));
        this.commentRepo.delete(comment);
    }
}
