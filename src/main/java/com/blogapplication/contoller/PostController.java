package com.blogapplication.contoller;


import com.blogapplication.config.AppConstant;
import com.blogapplication.payload.ApiResponse;
import com.blogapplication.payload.PostDto;
import com.blogapplication.payload.PostResponse;
import com.blogapplication.service.PostService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class PostController {

    @Autowired
    private PostService postService;

//    create post
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto>createpost(@RequestBody PostDto postDto, @PathVariable Integer userId,@PathVariable Integer categoryId){
        PostDto createPost=this.postService.creatPost(postDto,userId,categoryId);
        return new ResponseEntity<>(createPost, HttpStatus.CREATED);
    }

//    update post
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto>updatePost(@RequestBody PostDto postDto,@PathVariable Integer postId){
        PostDto updatePost=this.postService.updatePost(postDto,postId);
        return new ResponseEntity<>(updatePost,HttpStatus
                .OK);

    }

//    Get All post by Category
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>>getPostByCategory(@PathVariable Integer categoryId){
        List<PostDto>posts=this.postService.getAllpostbyCategory(categoryId);
        return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
    }
    //    Get All post by User
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDto>>getPostByUserId(@PathVariable Integer userId){
        List<PostDto>posts=this.postService.getAllpostbyUser(userId);
        return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
    }

//    delete post by id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/posts/{postId}")
    public ApiResponse deletePost(@PathVariable Integer postId){
        this.postService
                .deletePost(postId);
        return new ApiResponse("Post is successfully deleted",true, MDC.get("requestId"));
    }

//    get post by id
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto>getAllPost(@PathVariable Integer postId){
        PostDto postDto=this.postService.getPostbyId(postId);
        return new ResponseEntity<>(postDto,HttpStatus.OK);
    }
//    get all post
    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPost(
            @RequestParam(value = "pageNumber",defaultValue = AppConstant.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstant.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(value = "direction",defaultValue = AppConstant.DIRECTION,required = false)String direction,
            @RequestParam(value = "sortBy",defaultValue = AppConstant.SORT_BY,required = false)String sortBy
    ) {
        PostResponse postResponse = this.postService.getAllPost(pageNumber,pageSize,sortBy,direction); // Ensure this method includes necessary mappings
        return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
    }


    @GetMapping("/posts/search/{keywords}")
    public ResponseEntity<List<PostDto>>searchPostByTitle(@PathVariable("keywords") String keyword){
        List<PostDto>postDtos=this.postService.searchPost(keyword);
        return new ResponseEntity<List<PostDto>>(postDtos,HttpStatus.OK);
    }

}
