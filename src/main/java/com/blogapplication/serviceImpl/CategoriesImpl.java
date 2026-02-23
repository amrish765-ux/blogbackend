package com.blogapplication.serviceImpl;

import com.blogapplication.Exception.ResourceNotFoundException;
import com.blogapplication.entities.Categories;
import com.blogapplication.payload.CategoryDto;
import com.blogapplication.repo.CategoryRepo;
import com.blogapplication.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoriesImpl implements CategoryService {

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private CategoryRepo categoryRepo;
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Categories cat=this.modelMapper.map(categoryDto,Categories.class);
        Categories addedCate=this.categoryRepo.save(cat);
        return this.modelMapper.map(addedCate,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoriesId) {
        Categories cat=this.categoryRepo
                .findById(categoriesId)
                .orElseThrow(()->new ResourceNotFoundException("category","Categrory",categoriesId));
        cat.setCategoriesTitle(categoryDto.getCategoriesTitle());
        cat.setCategoryDescription(categoryDto.getCategoryDescription());
        Categories updatedCat=this.categoryRepo.save(cat);
        return this.modelMapper.map(updatedCat,CategoryDto.class);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Categories cat=this.categoryRepo
                .findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("category","Categrory",categoryId));
        this.categoryRepo.delete(cat);
    }

    @Override
    public CategoryDto getCatagory(Integer categoryId) {
        Categories cat=this.categoryRepo
                .findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("category","Categrory",categoryId));

        return this.modelMapper.map(cat,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Categories>categories=this.categoryRepo.findAll();
        List<CategoryDto>catDtos=categories.stream().map((cat)->this.modelMapper.map(cat,CategoryDto.class))
                .toList();
        return catDtos;
    }
}
