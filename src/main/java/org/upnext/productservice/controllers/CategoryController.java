package org.upnext.productservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.contracts.categories.CategoryRequest;
import org.upnext.productservice.contracts.categories.CategoryResponse;
import org.upnext.productservice.services.CategoryServices;
import org.upnext.sharedlibrary.Errors.Error;
import org.upnext.sharedlibrary.Errors.Result;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {

    private final CategoryServices categoryServices;


    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAllCategories(Pageable pageable) {
        Result<List<CategoryResponse>> result = categoryServices.findAllCategories(pageable);
        return ResponseEntity.ok(result.getValue());
    }

    @PostMapping
    public ResponseEntity createCategory(@RequestBody CategoryRequest categoryRequest, UriComponentsBuilder uriBuilder) {
        Result<URI> result = categoryServices.createNewCategory(categoryRequest, uriBuilder);

        if (result.isSuccess()) {
            return ResponseEntity.created(result.getValue()).build();
        }

        return response(result.getError());

    }

    @GetMapping("/{id}")
    public ResponseEntity getCategory(@PathVariable Long id) {
        Result<CategoryResponse> result = categoryServices.findCategoryById(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        }
        return response(result.getError());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity getCategoryByName(@PathVariable String name) {
        Result<CategoryResponse> result = categoryServices.findCategoryByName(name);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        }
        return response(result.getError());
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        Result result =  categoryServices.updateCategory(id, categoryRequest);
        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }
        return response(result.getError());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        Result result = categoryServices.deleteCategory(id);
        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }
        return response(result.getError());
    }

    private ResponseEntity response(Error error) {
        int status = error.getStatusCode();
        String errorMessage = error.getMessage();
        return ResponseEntity.status(status).body(errorMessage);
    }
}
