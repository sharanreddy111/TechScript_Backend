package com.techscript.blog.services.impl;

import com.techscript.blog.entities.Category;
import com.techscript.blog.entities.User;
import com.techscript.blog.exceptions.ResourceNotFoundException;
import com.techscript.blog.payloads.CategoryDTO;
import com.techscript.blog.payloads.UserDTO;
import com.techscript.blog.repositories.CategoryRepository;
import com.techscript.blog.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = this.modelMapper.map(categoryDTO, Category.class); //DtoToEntity
        Category savedCategory = this.categoryRepository.save(category);
        return this.modelMapper.map(savedCategory, CategoryDTO.class);  //EntityToDto
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer categoryId) {

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

        category.setCategoryTitle(category.getCategoryTitle());
        category.setCategoryDescription(category.getCategoryDescription());

        Category updatedCategory = this.categoryRepository.save(category);
        
        return this.modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));
        this.categoryRepository.delete(category);
    }

    @Override
    public CategoryDTO getCategoryById(Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));
        return this.modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = this.categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = categories.stream().map((category -> this.modelMapper.map(category, CategoryDTO.class)))
                .collect(Collectors.toList());
        return categoryDTOs;
    }
}
