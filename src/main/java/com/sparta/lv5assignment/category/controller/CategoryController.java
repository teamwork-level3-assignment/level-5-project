package com.sparta.lv5assignment.category.controller;


import com.sparta.lv5assignment.category.dto.CategoryRequestDto;
import com.sparta.lv5assignment.category.dto.CategoryResponseDto;
import com.sparta.lv5assignment.category.service.CategoryService;
import com.sparta.lv5assignment.global.dto.StatusEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<List<CategoryResponseDto>> createCategories(
            @RequestBody CategoryRequestDto categoryRequestDto
    ) {
        List<CategoryResponseDto> categoryResponseDtoList = categoryService.createCategories(categoryRequestDto);
        return new ResponseEntity<>(categoryResponseDtoList, HttpStatus.CREATED);
    }
}
