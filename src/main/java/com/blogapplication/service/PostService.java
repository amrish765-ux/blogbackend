package com.blogapplication.service;

import com.blogapplication.payload.PostDto;
import com.blogapplication.payload.PostResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
//    create
PostDto creatPost(PostDto  postDto,Integer userId,Integer categoryId);
//    update
PostDto updatePost(PostDto  postDto,Integer postId);

//    delete
void deletePost(Integer postId);

//    getAll posts
PostResponse getAllPost(Integer pageNumber, Integer pageSize,String sortBy,String sortDirection);

//    get single post
PostDto getPostbyId(Integer postId);

//    get all post category wise
List<PostDto>getAllpostbyCategory(Integer categoryId);

//    get all post user wise
List<PostDto>getAllpostbyUser(Integer userId);

//    search
    List<PostDto> searchPost(String keyword);

}
