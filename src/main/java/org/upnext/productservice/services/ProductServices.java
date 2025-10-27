package org.upnext.productservice.services;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.configurations.RabbitMQConfig;
import org.upnext.productservice.contracts.products.ProductRequest;
import org.upnext.productservice.contracts.products.ProductResponse;
import org.upnext.productservice.entities.Brand;
import org.upnext.productservice.entities.Category;
import org.upnext.productservice.entities.Product;

import static org.upnext.productservice.errors.BrandErrors.BrandNotFound;
import static org.upnext.productservice.errors.CategoryErrors.CategoryNotFound;
import static org.upnext.productservice.errors.ProductErrors.*;
import static org.upnext.productservice.helpers.ProductSpecification.filters;

import org.upnext.productservice.mappers.ProductMapper;
import org.upnext.productservice.repositories.BrandRepository;
import org.upnext.productservice.repositories.CategoryRepository;
import org.upnext.productservice.repositories.ProductRepository;
import org.upnext.sharedlibrary.Errors.Result;
import org.upnext.sharedlibrary.Events.ProductEvent;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServices {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductMapper productMapper;
    private final RabbitTemplate rabbitTemplate;
    @Value("${file.upload-dir}")
    private String uploadDir;



    public Result<ProductResponse> getProduct(Long id) {

        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return Result.failure(ProductNotFound);
        }

        return  Result.success(productMapper.toProductResponse(product));
    }

    public Result<URI> save
            (
                    ProductRequest productDto,
                    MultipartFile image,
                    HttpServletRequest request,
                    UriComponentsBuilder urb
            ) {


        String imageUrl = saveImage(image, request);
        Result<Product> result = getCategoryAndBrand(productDto);
        if (!result.isSuccess()) {
            return Result.failure(result.getError());
        }

        Product product = result.getValue();
        product.setImageUrl(imageUrl);
        Product product1 = productRepository.save(product);

        ProductEvent productEvent = productMapper.toProductEvent(product1);
        productEvent.setUrl("/products/" + product.getId());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_EXCHANGE,
                RabbitMQConfig.PRODUCT_KEY,
                productEvent);
//        URI uri = URI.create("/products/" + product1.getId());
        URI uri = urb
                .path("/products/{id}")
                .buildAndExpand(product1.getId())
                .toUri();
        return Result.success(uri);
    }

    protected void publishEvent(ProductEvent productEvent) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_EXCHANGE,
                RabbitMQConfig.PRODUCT_KEY,
                productEvent);
    }

    public Result<List<ProductResponse>>
    findAllFilteredProducts(
            String word,
            String category,
            String brand,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    )
    {
        Specification<Product> specification = filters(word,  category, brand, minPrice, maxPrice);
        Page<Product> page = productRepository.findAll(specification,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                )
        );

        return Result.success(productMapper.toProductResponseList(page.getContent()));
    }

    public Result<Void> updateProduct(Long id, ProductRequest productDto, MultipartFile image, HttpServletRequest request) {
        String imageUrl = null;
        if (image.getSize() != 0) {
            imageUrl = saveImage(image, request);
        }
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return Result.failure(ProductNotFound);
        }


        Result<Product> result = getCategoryAndBrand(productDto);
        if (!result.isSuccess()) {
            return Result.failure(result.getError());
        }
        Product product1 = result.getValue();

        product1.setId(product.getId());
        if (imageUrl != null) {
            product1.setImageUrl(imageUrl);
        }
        productRepository.save(product1);
        return Result.success();
    }

    public Result<Void> delete(Long id) {
        boolean exists = productRepository.existsById(id);
        if (!exists) {
            return Result.failure(ProductNotFound);
        }
        productRepository.deleteById(id);
        return Result.success();
    }
    protected String saveImage(MultipartFile image, HttpServletRequest request) {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {

            }
        }

        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        try {
            Files.copy(image.getInputStream(), filePath);
        } catch (IOException e) {

        }

        String baseUrl = request.getScheme() + "://" +
                request.getServerName() + ":" +
                request.getServerPort();

        String fullPath = baseUrl + "/images/" + fileName;
        return  fullPath;
    }

    public Result<Void> decreaseStock(Long id, Integer amount) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return Result.failure(ProductNotFound);
        }
        product.setStock(product.getStock() + amount);
        return Result.success();
    }

    private Result<Product> getCategoryAndBrand(ProductRequest productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        Brand brand = brandRepository.findById(productDto.getBrandId()).orElse(null);
        if (category == null ) {
            return Result.failure(CategoryNotFound);
        }
        if (brand == null ) {
            return Result.failure(BrandNotFound);
        }

        Product product = productMapper.toProduct(productDto);

        product.setCategory(category);
        product.setBrand(brand);
        return Result.success(product);
    }
}
