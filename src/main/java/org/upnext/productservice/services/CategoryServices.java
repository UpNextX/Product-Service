package org.upnext.productservice.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.contracts.categories.CategoryRequest;
import org.upnext.productservice.contracts.categories.CategoryResponse;
import org.upnext.productservice.entities.Category;
import org.upnext.productservice.mappers.CategoryMapper;
import org.upnext.productservice.repositories.CategoryRepository;
import org.upnext.sharedlibrary.Errors.Result;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static org.upnext.productservice.errors.CategoryErrors.*;

@Service
@RequiredArgsConstructor
public class CategoryServices {

    public final CategoryRepository categoryRepository;
    public final CategoryMapper  categoryMapper;


    public Result<CategoryResponse> findCategoryById(Long id) {
        Category category = findByCategoryId(id);

        if (category == null) {
            return Result.failure(CategoryNotFound);
        }

        return Result.success(categoryMapper.toCategoryResponse(category));
    }


    public Result<List<CategoryResponse>> findAllCategories(Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                )
        );
        return Result.success(categoryMapper.toCategoryResponseList(page.getContent()));
    }


    public Result<CategoryResponse> findCategoryByName(String name) {
        Category category = findByName(name);

        if (category == null) {
            return Result.failure(CategoryNotFound);
        }

        return Result.success(categoryMapper.toCategoryResponse(category));
    }


    public Result<URI> createNewCategory(CategoryRequest categoryRequest, UriComponentsBuilder urb) {

        Category category = findByName(categoryRequest.getName());
        if (category != null) {
            return Result.failure(DuplicatedCategoryName);
        }

        category = categoryMapper.toCategory(categoryRequest);
        category = categoryRepository.save(category);

        URI uri = urb.path("/categories/{id}")
                .buildAndExpand(category.getId())
                .toUri();
        return Result.success(uri);
    }

    public Result<Void> updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = findByCategoryId(id);
        if  (category == null) {
            return Result.failure(CategoryNotFound);
        }

        category = findByName(categoryRequest.getName());

        if (category != null && !Objects.equals(id, category.getId())) {
            return Result.failure(DuplicatedCategoryName);
        }

        category = categoryMapper.toCategory(categoryRequest);
        category.setId(id);
        categoryRepository.save(category);

        return  Result.success();
    }

    public Result<Void> deleteCategory(Long id) {
        Category category = findByCategoryId(id);
        if (category == null) {
            return Result.failure(CategoryNotFound);
        }
        categoryRepository.delete(category);
        return Result.success();
    }

    private Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    private Category findByCategoryId(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

}
