package com.blogapplication.contoller;

import com.blogapplication.payload.ApiResponse;
import com.blogapplication.payload.UserDto;
import com.blogapplication.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

//    @PostMapping("/")
//    public ResponseEntity<UserDto>createUser(@Valid @RequestBody UserDto userDto){
//        UserDto createdUserDto=this.userService.createUser(userDto);
//        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED  );
//    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto>updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Integer userId){
        UserDto updateUser=this.userService.updateUser(userDto,userId);
        return ResponseEntity.ok(updateUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>>getAllUser(){
        return ResponseEntity.ok(this.userService.getAllUser());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto>getSingleUser(@PathVariable Integer userId){
        return ResponseEntity.ok(this.userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse>deleteUser(@PathVariable Integer userId){
        this.userService.deleteUser(userId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("user deleted successfully",true),HttpStatus.OK);
    }
}
