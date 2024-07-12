package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.repository.CategoryRepository;
import com.ecommerce.library.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repo;

    @Override
    public List<Category> findAll() {
        return repo.findAll();
    }

    @Override
    public Category save(Category category) {
        try{
            Category categorySave = new Category(category.getName());
            return repo.save(categorySave);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    @Override
    public Optional<Category> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Category update(Category category) {
        Category categoryUpdate = repo.getReferenceById(category.getId());
        categoryUpdate.setName(category.getName());
        return repo.save(categoryUpdate);
    }

    @Override
    public void deletedById(Long id) {
       Category category = repo.getReferenceById(id);

       category.set_deleted(true);
       category.set_activated(false);
       repo.save(category);
    }

    @Override
    public void enabledById(Long id) {
          Category category = repo.getById(id);
          category.set_activated(true);
          category.set_deleted(false);
          repo.save(category);
    }

    @Override
    public List<Category> findAllByActivated() {
        return repo.findAllByActivated();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public boolean existsByName(String name) {
        return repo.existsByName(name);
    }

    @Override
    public List<CategoryDto> getCategoriesAndSize() {
        return repo.getCategoriesAndSize();
    }
}
