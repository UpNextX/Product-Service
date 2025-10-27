package org.upnext.productservice.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.contracts.products.ProductRequest;
import org.upnext.productservice.contracts.products.ProductResponse;
import org.upnext.productservice.services.ProductServices;
import org.upnext.sharedlibrary.Dtos.StockUpdateRequest;
import org.upnext.sharedlibrary.Errors.Error;
import org.upnext.sharedlibrary.Errors.Result;



import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServices productServices;

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        Result<ProductResponse> result = productServices.getProduct(id);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        }

        return response(result.getError());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity save
            (
                    @Valid @RequestPart ProductRequest product,
                    @RequestPart MultipartFile image,
                    HttpServletRequest request,
                    UriComponentsBuilder urb,
                    Authentication authentication
            )
    {
        Result<URI> result = productServices.save(product, image, request, urb);
        if (result.isSuccess()) {
            return ResponseEntity.created(result.getValue()).build();
        }

        return response(result.getError());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteById(@PathVariable Long id) {
        Result result = productServices.delete(id);
        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }

        return response(result.getError());
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestPart ProductRequest product, @RequestPart MultipartFile image, HttpServletRequest request) {
        Result result = productServices.updateProduct(id, product, image, request);
        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }
        return response(result.getError());
    }


    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(
            @RequestParam(required = false) String word,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable) {
        Result<List<ProductResponse>> result = productServices.findAllFilteredProducts(word, category, brand, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(result.getValue());
    }


    @PutMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @Valid @RequestPart StockUpdateRequest request) {
        Result result = productServices.decreaseStock(id, request.getStock());
        return (result.isSuccess()) ? ResponseEntity.noContent().build() : response(result.getError());
    }

    private ResponseEntity response(Error error) {
        int status = error.getStatusCode();
        String errorMessage = error.getMessage();
        return ResponseEntity.status(status).body(errorMessage);
    }

}
