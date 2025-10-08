package org.upnext.productservice.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.contracts.products.ProductRequest;
import org.upnext.productservice.contracts.products.ProductResponse;
import org.upnext.sharedlibrary.Dtos.StockUpdateRequest;
import org.upnext.productservice.entities.Product;
import org.upnext.productservice.services.ProductServices;
import org.upnext.sharedlibrary.Errors.Error;
import org.upnext.sharedlibrary.Errors.Result;



import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductServices productServices;
    public ProductController(ProductServices productServices) {
        this.productServices = productServices;
    }


    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(Pageable pageable) {
        Result<List<ProductResponse>> result = productServices.getProducts(pageable);

        return ResponseEntity.ok(result.getValue());
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        Result<ProductResponse> result = productServices.getProduct(id);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        }

        return response(result.getError());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity save
            (
                    @Valid @RequestPart ProductRequest product,
                    @RequestPart MultipartFile image,
                    HttpServletRequest request,
                    UriComponentsBuilder urb
            )
    {
        Result<URI> result = productServices.save(product, image, request, urb);
        if (result.isSuccess()) {
            return ResponseEntity.created(result.getValue()).build();
        }

        return response(result.getError());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        Result result = productServices.delete(id);
        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }

        return response(result.getError());
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestPart ProductRequest product, @RequestPart MultipartFile image, HttpServletRequest request) {
        Result result = productServices.updateProduct(id, product, image, request);
        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }
        return response(result.getError());
    }

    @PutMapping(value="/{id}/stock/decrease")
    public ResponseEntity updateStock(@PathVariable Long id, @Valid @RequestBody StockUpdateRequest stockUpdateRequest){
        Result result = productServices.updateProductStock(id, -stockUpdateRequest.getStock());
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
