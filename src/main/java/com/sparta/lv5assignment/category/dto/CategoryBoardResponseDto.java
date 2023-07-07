package com.sparta.lv5assignment.category.dto;

import com.sparta.lv5assignment.category.entity.CategoryBoard;
import lombok.Getter;


@Getter
public class CategoryBoardResponseDto {
    private String categoryName;

    public CategoryBoardResponseDto(String categoryName) {
        this.categoryName = categoryName;
    }
}
