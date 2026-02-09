package com.blogapplication.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Integer categoriesId;

    @NotBlank
    @Size(min=4,message = "min size of category is 4")
    private String categoriesTitle;

    @NotBlank
    @Size(min=10,message = "min size of category is 10")
     private String categoryDescription;

}
