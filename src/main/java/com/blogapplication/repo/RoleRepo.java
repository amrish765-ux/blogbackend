package com.blogapplication.repo;

import com.blogapplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepo extends JpaRepository<User,Integer> {
    public Optional<User>findByEmail(String email);
    boolean existsByEmail(String email);

}
