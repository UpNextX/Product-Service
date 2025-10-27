package org.upnext.productservice.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.commons.config.DefaultsBindHandlerAdvisor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.contracts.motherboards.MotherboardRequest;
import org.upnext.productservice.contracts.motherboards.MotherboardResponse;
import org.upnext.productservice.entities.Brand;
import org.upnext.productservice.entities.Category;
import org.upnext.productservice.entities.Motherboard;
import org.upnext.productservice.mappers.MotherboardMapper;
import org.upnext.productservice.mappers.ProductMapper;
import org.upnext.productservice.repositories.BrandRepository;
import org.upnext.productservice.repositories.CategoryRepository;
import org.upnext.productservice.repositories.MotherboardRepository;
import org.upnext.sharedlibrary.Errors.Result;
import org.upnext.sharedlibrary.Events.ProductEvent;

import java.net.URI;
import java.util.List;

import static org.upnext.productservice.errors.BrandErrors.BrandNotFound;
import static org.upnext.productservice.errors.CategoryErrors.CategoryNotFound;
import static org.upnext.productservice.errors.ProductErrors.ProductNotFound;

@Service
@RequiredArgsConstructor
public class MotherboardServices {

    private final MotherboardRepository motherboardRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final MotherboardMapper motherboardMapper;
    private final ProductServices productServices;
    private final DefaultsBindHandlerAdvisor.MappingsProvider mappingsProvider;
    private final ProductMapper productMapper;

    public Result<List<MotherboardResponse>> findAll(Pageable pageable) {
        Page<Motherboard> page = motherboardRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
        ));

        List<MotherboardResponse> responses = motherboardMapper.toResponseList(page.getContent());
        return Result.success(responses);
    }

    public Result<MotherboardResponse> findById(Long id) {
        return motherboardRepository.findById(id)
                .map(mb -> Result.success(motherboardMapper.toResponse(mb)))
                .orElse(Result.failure(ProductNotFound));
    }

    public Result<URI> create(MotherboardRequest dto, MultipartFile image,
                              HttpServletRequest request,
                              UriComponentsBuilder urb) {
        String imageUrl = productServices.saveImage(image, request);

        Result<Motherboard> result = getCategoryAndBrand(dto);
        if (!result.isSuccess()) return Result.failure(result.getError());

        Motherboard mb = result.getValue();
        mb.setImageUrl(imageUrl);
        Motherboard saved = motherboardRepository.save(mb);

        ProductEvent event = productMapper.toProductEvent(saved);
        event.setUrl("/motherboards/" + saved.getId());
        productServices.publishEvent(event);

        URI uri = urb.path("/motherboards/{id}").buildAndExpand(saved.getId()).toUri();
        return Result.success(uri);
    }

    public Result<Void> update(Long id, MotherboardRequest dto, MultipartFile image, HttpServletRequest request) {
        String imageUrl = (image.getSize() != 0) ? productServices.saveImage(image, request) : null;
        Motherboard existing = motherboardRepository.findById(id).orElse(null);
        if (existing == null) return Result.failure(ProductNotFound);

        Result<Motherboard> result = getCategoryAndBrand(dto);
        if (!result.isSuccess()) return Result.failure(result.getError());

        Motherboard updated = result.getValue();
        updated.setId(existing.getId());
        if (imageUrl != null) updated.setImageUrl(imageUrl);
        motherboardRepository.save(updated);
        return Result.success();
    }

    public Result<Void> delete(Long id) {
        if (!motherboardRepository.existsById(id)) return Result.failure(ProductNotFound);
        motherboardRepository.deleteById(id);
        return Result.success();
    }

    private Result<Motherboard> getCategoryAndBrand(MotherboardRequest dto) {
        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        Brand brand = brandRepository.findById(dto.getBrandId()).orElse(null);
        if (category == null) return Result.failure(CategoryNotFound);
        if (brand == null) return Result.failure(BrandNotFound);

        Motherboard mb = motherboardMapper.toEntity(dto);
        mb.setCategory(category);
        mb.setBrand(brand);
        return Result.success(mb);
    }
}
