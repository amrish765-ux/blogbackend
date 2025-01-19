package com.blogapplication.service;

import com.blogapplication.payload.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
//    create
    CategoryDto createCategory(CategoryDto categoryDto);
//    update
CategoryDto updateCategory(CategoryDto categoryDto,Integer categoriesId);
//    delete
    void deleteCategory(Integer categoryId);
//    get
    CategoryDto getCatagory(Integer categoryId);
//    getAll
    List<CategoryDto>getAllCategories();
}
