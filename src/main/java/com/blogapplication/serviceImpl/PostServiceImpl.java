package com.blogapplication.serviceImpl;

import com.blogapplication.Exception.ResourceNotFoundException;
import com.blogapplication.entities.Categories;
import com.blogapplication.entities.Post;
import com.blogapplication.entities.User;
import com.blogapplication.payload.CategoryDto;
import com.blogapplication.payload.PostDto;
import com.blogapplication.payload.PostResponse;
import com.blogapplication.payload.UserDto;
import com.blogapplication.repo.CategoryRepo;
import com.blogapplication.repo.PostRepo;
import com.blogapplication.repo.UserRepo;
import com.blogapplication.service.PostService;
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
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;
    @Override
    public PostDto creatPost(PostDto postDto,Integer userId,Integer categoryId) {
        User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","user id",userId));
        Categories categories=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category","category id",categoryId));
        Post post=this.modelMapper.map(postDto,Post.class);

        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setCategories(categories);
        post.setUser(user);
        Post newPost=this.postRepo.save(post);

        PostDto newPostDto=this.modelMapper.map(newPost,PostDto.class);
        newPostDto.setCategoryDto(this.modelMapper.map(categories, CategoryDto.class));
        newPostDto.setUserDto(this.modelMapper.map(user, UserDto.class));
        return newPostDto;
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","postid",postId));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageName(postDto.getImageName());

        Post update=this.postRepo.save(post);


        PostDto updatedPostDto = this.modelMapper.map(update, PostDto.class);
        updatedPostDto.setUserDto(this.modelMapper.map(post.getUser(), UserDto.class));
        updatedPostDto.setCategoryDto(this.modelMapper.map(post.getCategories(), CategoryDto.class));

        return updatedPostDto;
    }

    @Override
    public void deletePost(Integer postId) {
        Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","postid",postId));
        this.postRepo.delete(post);
    }

    @Override
    public PostResponse getAllPost(Integer pageNumber,Integer pageSize,String sortBy,String sortDirection) {
//        Sort.Direction direction=Sort.Direction.fromString(sortDirection);
        Sort sort=null;
        if(sortDirection.equalsIgnoreCase("asc")){
            sort=Sort.by(sortBy).ascending();
        }else {
            sort=Sort.by(sortBy).descending();
        }
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Post>pagePost=this.postRepo.findAll(pageable);

        List<Post> posts = pagePost.getContent();

        List<PostDto>postDtots=posts.stream().map((post)->{
           PostDto postDto= this.modelMapper.map(post,PostDto.class);
           if (post.getCategories()!=null){
               postDto.setCategoryDto(this.modelMapper.map(post.getCategories(),CategoryDto.class));
           }
           if(post.getUser()!=null){
               postDto.setUserDto(this.modelMapper.map(post.getUser(),UserDto.class));
           }
           return postDto;
        }).toList();
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(postDtots);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setLastPage(pagePost.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostbyId(Integer postId) {
        Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","postid",postId));

        PostDto postDto=this.modelMapper.map(post,PostDto.class);
        if(post.getUser()!=null){
            postDto.setUserDto(this.modelMapper.map(post.getUser(), UserDto.class));
        }
        if(post.getCategories()!=null){
            postDto.setCategoryDto(this.modelMapper.map(post.getCategories(),CategoryDto.class));
        }
        return postDto;
    }

    @Override
    public List<PostDto> getAllpostbyCategory(Integer categoryId) {
        Categories cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "CategoryId", categoryId));

        List<Post> posts = this.postRepo.findByCategories(cat);

        List<PostDto> postDtos = posts.stream().map(post -> {
            PostDto postDto = this.modelMapper.map(post, PostDto.class);

            // Manually map category and user if they are not automatically mapped
            if (post.getCategories() != null) {
                postDto.setCategoryDto(this.modelMapper.map(post.getCategories(), CategoryDto.class));
            }
            if (post.getUser() != null) {
                postDto.setUserDto(this.modelMapper.map(post.getUser(), UserDto.class));
            }

            return postDto;
        }).toList();

        return postDtos;
    }


    @Override
    public List<PostDto> getAllpostbyUser(Integer userId) {
        User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","userid",userId));
        List<Post>posts=this.postRepo.findByUser(user);
        List<PostDto>postDtos=posts.stream().map(post->{
            PostDto postDto=this.modelMapper.map(post,PostDto.class);
            if(post.getUser()!=null){
                postDto.setUserDto(this.modelMapper.map(post.getUser(),UserDto.class));
            }
            if(post.getCategories()!=null){
                postDto.setCategoryDto(this.modelMapper.map(post.getCategories(),CategoryDto.class));
            }
            return postDto;

        }).toList();
        return postDtos;
    }

    @Override
    public List<PostDto> searchPost(String keyword) {
        List<Post>posts=this.postRepo.findByTitleContaining(keyword);
        List<PostDto>postDtos=posts.stream().map(post->{
            PostDto postDto=this.modelMapper.map(post,PostDto.class);
            if(post.getUser()!=null){
                postDto.setUserDto(this.modelMapper.map(post.getUser(),UserDto.class));
            }
            if(post.getCategories()!=null){
                postDto.setCategoryDto(this.modelMapper.map(post.getCategories(),CategoryDto.class));
            }
            return postDto;

        }).toList();
        return postDtos;
    }
}
