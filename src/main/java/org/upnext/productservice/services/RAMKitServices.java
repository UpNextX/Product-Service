package org.upnext.productservice.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.commons.config.DefaultsBindHandlerAdvisor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.contracts.ramkits.RAMKitRequest;
import org.upnext.productservice.contracts.ramkits.RAMKitResponse;
import org.upnext.productservice.entities.Brand;
import org.upnext.productservice.entities.Category;
import org.upnext.productservice.entities.RAMKit;
import org.upnext.productservice.mappers.ProductMapper;
import org.upnext.productservice.mappers.RAMKitMapper;
import org.upnext.productservice.repositories.BrandRepository;
import org.upnext.productservice.repositories.CategoryRepository;
import org.upnext.productservice.repositories.RAMKitRepository;
import org.upnext.sharedlibrary.Errors.Result;
import org.upnext.sharedlibrary.Events.ProductEvent;

import java.net.URI;
import java.util.List;

import static org.upnext.productservice.errors.BrandErrors.BrandNotFound;
import static org.upnext.productservice.errors.CategoryErrors.CategoryNotFound;
import static org.upnext.productservice.errors.ProductErrors.ProductNotFound;

@Service
@RequiredArgsConstructor
public class RAMKitServices {

    private final RAMKitRepository ramKitRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final RAMKitMapper ramKitMapper;
    private final ProductServices productServices;
    private final DefaultsBindHandlerAdvisor.MappingsProvider mappingsProvider;
    private final ProductMapper productMapper;

    public Result<List<RAMKitResponse>> findAll(Pageable pageable) {
        Page<RAMKit> page = ramKitRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
        ));
        return Result.success(ramKitMapper.toResponseList(page.getContent()));
    }

    public Result<RAMKitResponse> findById(Long id) {
        return ramKitRepository.findById(id)
                .map(r -> Result.success(ramKitMapper.toResponse(r)))
                .orElse(Result.failure(ProductNotFound));
    }

    public Result<URI> create(RAMKitRequest dto, MultipartFile image,
                              HttpServletRequest request,
                              UriComponentsBuilder urb) {
        String imageUrl = productServices.saveImage(image, request);
        Result<RAMKit> result = getCategoryAndBrand(dto);
        if (!result.isSuccess()) return Result.failure(result.getError());

        RAMKit entity = result.getValue();
        entity.setImageUrl(imageUrl);
        RAMKit saved = ramKitRepository.save(entity);

        ProductEvent event = productMapper.toProductEvent(saved);
        event.setUrl("http://localhost:5173/ramkits/" + saved.getId());
        productServices.publishEvent(event);

        URI uri = urb.path("/ramkits/{id}").buildAndExpand(saved.getId()).toUri();
        return Result.success(uri);
    }

    public Result<Void> update(Long id, RAMKitRequest dto, MultipartFile image, HttpServletRequest request) {
        String imageUrl = (image.getSize() != 0) ? productServices.saveImage(image, request) : null;
        RAMKit existing = ramKitRepository.findById(id).orElse(null);
        if (existing == null) return Result.failure(ProductNotFound);

        Result<RAMKit> result = getCategoryAndBrand(dto);
        if (!result.isSuccess()) return Result.failure(result.getError());

        RAMKit updated = result.getValue();
        updated.setId(existing.getId());
        if (imageUrl != null) updated.setImageUrl(imageUrl);
        ramKitRepository.save(updated);
        return Result.success();
    }

    public Result<Void> delete(Long id) {
        if (!ramKitRepository.existsById(id)) return Result.failure(ProductNotFound);
        ramKitRepository.deleteById(id);
        return Result.success();
    }

    private Result<RAMKit> getCategoryAndBrand(RAMKitRequest dto) {
        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        Brand brand = brandRepository.findById(dto.getBrandId()).orElse(null);
        if (category == null) return Result.failure(CategoryNotFound);
        if (brand == null) return Result.failure(BrandNotFound);

        RAMKit entity = ramKitMapper.toEntity(dto);
        entity.setCategory(category);
        entity.setBrand(brand);
        return Result.success(entity);
    }
}
