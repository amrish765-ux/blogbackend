package com.blogapplication.serviceImpl;

import com.blogapplication.Exception.ResourceNotFoundException;
import com.blogapplication.entities.User;
import com.blogapplication.payload.UserDto;
import com.blogapplication.repo.UserRepo;
import com.blogapplication.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSeviceImpl implements UserService {


    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user=this.modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User addedUser=this.userRepo.save(user);
        return this.modelMapper.map(addedUser,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User cat = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "user id", userId));
        cat.setName(userDto.getName());
        cat.setEmail(userDto.getEmail());
        cat.setPassword(userDto.getPassword());
        cat.setAge(userDto.getAge());
        cat.setGender(userDto.getGender());
        User updateduser=this.userRepo.save(cat);
        return this.modelMapper.map(updateduser,UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User cat = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "user id", userId));

        return this.modelMapper.map(cat,UserDto.class);
    }

    @Override
    public List<UserDto> getAllUser() {
        List<User>users=this.userRepo.findAll();
        return users
                .stream()
                .map((cat)-> this.modelMapper.map(cat,UserDto.class))
                .toList();
    }

    @Override
    public void deleteUser(Integer userId) {
        User cat = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "user id", userId));
        this.userRepo.delete(cat);
    }
}
