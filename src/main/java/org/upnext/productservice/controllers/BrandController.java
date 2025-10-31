package org.upnext.productservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.contracts.brands.BrandRequest;
import org.upnext.productservice.contracts.brands.BrandResponse;
import org.upnext.productservice.services.BrandServices;
import org.upnext.sharedlibrary.Errors.Error;
import org.upnext.sharedlibrary.Errors.Result;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandServices BrandServices;


    @GetMapping
    public ResponseEntity<List<BrandResponse>> findAllBrands(Pageable pageable) {
        Result<List<BrandResponse>> result = BrandServices.findAllBrands(pageable);
        return ResponseEntity.ok(result.getValue());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createBrand(@RequestBody BrandRequest BrandRequest, UriComponentsBuilder uriBuilder) {
        Result<URI> result = BrandServices.createNewBrand(BrandRequest, uriBuilder);

        if (result.isSuccess()) {
            return ResponseEntity.created(result.getValue()).build();
        }

        return response(result.getError());

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getBrand(@PathVariable Long id) {
        Result<BrandResponse> result = BrandServices.findBrandById(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        }
        return response(result.getError());
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getBrandByName(@PathVariable String name) {
        Result<BrandResponse> result = BrandServices.findBrandByName(name);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        }
        return response(result.getError());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateBrand(@PathVariable Long id, @RequestBody BrandRequest BrandRequest) {
        Result<Void> result =  BrandServices.updateBrand(id, BrandRequest);
        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }
        return response(result.getError());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBrand(@PathVariable Long id) {
        Result<Void> result = BrandServices.deleteBrand(id);
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
