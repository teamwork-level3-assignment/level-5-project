package com.sparta.lv5assignment.category.service;


import com.sparta.lv5assignment.category.dto.CategoryRequestDto;
import com.sparta.lv5assignment.category.dto.CategoryResponseDto;
import com.sparta.lv5assignment.category.entity.Category;
import com.sparta.lv5assignment.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> createCategories(CategoryRequestDto categoryRequestDto) {
        List<Category> categoryList = new ArrayList<>();

        // 넘어온 카테고리 리스트를 하나씩 response 만들기
        List<String> categoryNames = categoryRequestDto.getCategoryNames();
        for (String categoryName : categoryNames) {
            categoryList.add(categoryRepository.save(new Category(categoryName)));
        }

        return categoryList.stream().map(CategoryResponseDto::new).toList();
    }
}
