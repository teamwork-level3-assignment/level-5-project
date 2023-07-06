package com.sparta.lv5assignment.category.dto;


import com.sparta.lv5assignment.category.entity.Category;
import lombok.Getter;

@Getter
public class CategoryResponseDto {

    private String categoryName;

    public CategoryResponseDto(Category category) {
        this.categoryName = category.getCategoryName();
    }
}
