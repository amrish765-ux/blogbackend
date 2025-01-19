package com.blogapplication.repo;

import com.blogapplication.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Categories,Integer> {
}
