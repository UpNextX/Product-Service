package org.upnext.productservice.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.commons.config.DefaultsBindHandlerAdvisor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.contracts.psus.PSURequest;
import org.upnext.productservice.contracts.psus.PSUResponse;
import org.upnext.productservice.entities.Brand;
import org.upnext.productservice.entities.Category;
import org.upnext.productservice.entities.PSU;
import org.upnext.productservice.mappers.PSUMapper;
import org.upnext.productservice.mappers.ProductMapper;
import org.upnext.productservice.repositories.BrandRepository;
import org.upnext.productservice.repositories.CategoryRepository;
import org.upnext.productservice.repositories.PSURepository;
import org.upnext.sharedlibrary.Errors.Result;
import org.upnext.sharedlibrary.Events.ProductEvent;

import java.net.URI;
import java.util.List;

import static org.upnext.productservice.errors.BrandErrors.BrandNotFound;
import static org.upnext.productservice.errors.CategoryErrors.CategoryNotFound;
import static org.upnext.productservice.errors.ProductErrors.ProductNotFound;

@Service
@RequiredArgsConstructor
public class PSUServices {

    private final PSURepository psuRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final PSUMapper psuMapper;
    private final ProductServices productServices;
    private final DefaultsBindHandlerAdvisor.MappingsProvider mappingsProvider;
    private final ProductMapper productMapper;

    public Result<List<PSUResponse>> findAll(Pageable pageable) {
        Page<PSU> page = psuRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
        ));
        return Result.success(psuMapper.toResponseList(page.getContent()));
    }

    public Result<PSUResponse> findById(Long id) {
        return psuRepository.findById(id)
                .map(p -> Result.success(psuMapper.toResponse(p)))
                .orElse(Result.failure(ProductNotFound));
    }

    public Result<URI> create(PSURequest dto, MultipartFile image,
                              HttpServletRequest request,
                              UriComponentsBuilder urb) {
        String imageUrl = productServices.saveImage(image, request);
        Result<PSU> result = getCategoryAndBrand(dto);
        if (!result.isSuccess()) return Result.failure(result.getError());

        PSU entity = result.getValue();
        entity.setImageUrl(imageUrl);
        PSU saved = psuRepository.save(entity);

        ProductEvent event = productMapper.toProductEvent(saved);
        event.setUrl("http://localhost:5173/psus/" + saved.getId());
        productServices.publishEvent(event);

        URI uri = urb.path("/psus/{id}").buildAndExpand(saved.getId()).toUri();
        return Result.success(uri);
    }

    public Result<Void> update(Long id, PSURequest dto, MultipartFile image, HttpServletRequest request) {
        String imageUrl = (image.getSize() != 0) ? productServices.saveImage(image, request) : null;
        PSU existing = psuRepository.findById(id).orElse(null);
        if (existing == null) return Result.failure(ProductNotFound);

        Result<PSU> result = getCategoryAndBrand(dto);
        if (!result.isSuccess()) return Result.failure(result.getError());

        PSU updated = result.getValue();
        updated.setId(existing.getId());
        if (imageUrl != null) updated.setImageUrl(imageUrl);
        psuRepository.save(updated);
        return Result.success();
    }

    public Result<Void> delete(Long id) {
        if (!psuRepository.existsById(id)) return Result.failure(ProductNotFound);
        psuRepository.deleteById(id);
        return Result.success();
    }

    private Result<PSU> getCategoryAndBrand(PSURequest dto) {
        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        Brand brand = brandRepository.findById(dto.getBrandId()).orElse(null);
        if (category == null) return Result.failure(CategoryNotFound);
        if (brand == null) return Result.failure(BrandNotFound);

        PSU entity = psuMapper.toEntity(dto);
        entity.setCategory(category);
        entity.setBrand(brand);
        return Result.success(entity);
    }
}
