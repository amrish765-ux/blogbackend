package com.blogapplication.contoller;

import com.blogapplication.entities.User;
import com.blogapplication.payload.JwtRequest;
import com.blogapplication.payload.JwtResponse;
import com.blogapplication.payload.UserDto;
import com.blogapplication.security.JwtHelper;
import com.blogapplication.service.UserService;
import io.jsonwebtoken.Jwt;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService  userService;

    private Logger logger= LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        this.doAuthenticate(request.getUsername(),request.getPassword());
        UserDetails userDetails=userDetailsService.loadUserByUsername(request.getUsername());
        String token=this.jwtHelper.generateToken(userDetails);

        JwtResponse response= JwtResponse.builder()
                .token(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(username,password);
        try {
            authenticationManager.authenticate(authentication);
        }
        catch (BadCredentialsException e){
            throw new BadCredentialsException(" invalid username or password !!");
        }
    }
    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler(){
        return "credentials invalid!!";
    }

    @PostMapping("/create-user")
    public ResponseEntity<UserDto>createUser(@Valid @RequestBody UserDto userDto){
        UserDto createdUserDto=this.userService.createUser(userDto);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }
}
