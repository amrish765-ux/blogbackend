package com.blogapplication.service;

import com.blogapplication.entities.User;
import com.blogapplication.payload.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user,Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUser();
    void deleteUser(Integer userId);
}
