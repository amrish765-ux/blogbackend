package com.blogapplication.contoller;

import com.blogapplication.payload.ApiResponse;
import com.blogapplication.payload.CategoryDto;
import com.blogapplication.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/")
    public ResponseEntity<CategoryDto>createCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto createCategory=this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto>updateCategory(@Valid @RequestBody CategoryDto categoryDto,@PathVariable Integer categoryId){
        CategoryDto updatedCategory=this.categoryService.updateCategory(categoryDto,categoryId);
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto>getCategory(@PathVariable Integer catId){
        CategoryDto getCategoryDto=this.categoryService.getCatagory(catId);
        return new ResponseEntity<>(getCategoryDto,HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoryDto>>getCategoroies(){
        List<CategoryDto> getCategoryDto=this.categoryService.getAllCategories();
        return  ResponseEntity.ok(getCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<ApiResponse>deteleCategory(@PathVariable Integer catId){
        this.categoryService.deleteCategory(catId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("category successfully deleted!!",true),HttpStatus.OK);
    }
}
