package com.techscript.blog.services.impl;

import com.techscript.blog.entities.Category;
import com.techscript.blog.exceptions.ResourceNotFoundException;
import com.techscript.blog.payloads.CategoryDTO;
import com.techscript.blog.repositories.CategoryRepository;
import com.techscript.blog.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = this.dtoToEntity(categoryDTO);
        Category savedCategory = this.categoryRepository.save(category);
        return this.entityToDto(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

        category.setCategoryTitle(categoryDTO.getCategoryTitle());
        category.setCategoryDescription(categoryDTO.getCategoryDescription());

        Category updatedCategory = this.categoryRepository.save(category);
        
        return this.entityToDto(updatedCategory);
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
        return this.entityToDto(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = this.categoryRepository.findAll();
        return categories.stream().map(this::entityToDto).toList();
    }

    public Category dtoToEntity(CategoryDTO categoryDTO){
        return this.modelMapper.map(categoryDTO,Category.class);
    }

    public CategoryDTO entityToDto(Category category){
        return this.modelMapper.map(category, CategoryDTO.class);
    }
}