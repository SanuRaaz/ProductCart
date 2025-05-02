package com.cartclothing.dev.cart.Service.Category;

import com.cartclothing.dev.cart.Exception.AlreadyExistException;
import com.cartclothing.dev.cart.Exception.CategoryNotFoundException;
import com.cartclothing.dev.cart.Modal.Category;
import com.cartclothing.dev.cart.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

  private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException ("Category not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
             //   .orElseThrow(() -> new CategoryNotFoundException ("Category not found by this name"));
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll ();
    }

    @Override
    public Category addCategory(Category category) {
        //return Optional.of(categoryRepository.save(category)).orElseThrow ();
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save).orElseThrow(()->new AlreadyExistException ("Error due to the category exist"));
    }

    //888416 0000107132   02/32

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById (id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow (() -> new CategoryNotFoundException ("Category is not there with this id for the update"));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository :: delete, () -> {
            throw new CategoryNotFoundException ("Category not found");
        });

    }
}
