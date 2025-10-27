package org.upnext.productservice.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.commons.config.DefaultsBindHandlerAdvisor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.contracts.pccases.PCCaseRequest;
import org.upnext.productservice.contracts.pccases.PCCaseResponse;
import org.upnext.productservice.entities.Brand;
import org.upnext.productservice.entities.Category;
import org.upnext.productservice.entities.PCCase;
import org.upnext.productservice.mappers.PCCaseMapper;
import org.upnext.productservice.mappers.ProductMapper;
import org.upnext.productservice.repositories.BrandRepository;
import org.upnext.productservice.repositories.CategoryRepository;
import org.upnext.productservice.repositories.PCCaseRepository;
import org.upnext.sharedlibrary.Errors.Result;
import org.upnext.sharedlibrary.Events.ProductEvent;

import java.net.URI;
import java.util.List;

import static org.upnext.productservice.errors.BrandErrors.BrandNotFound;
import static org.upnext.productservice.errors.CategoryErrors.CategoryNotFound;
import static org.upnext.productservice.errors.ProductErrors.ProductNotFound;

@Service
@RequiredArgsConstructor
public class PCCaseServices {

    private final PCCaseRepository pcCaseRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final PCCaseMapper pcCaseMapper;
    private final ProductServices productServices;
    private final DefaultsBindHandlerAdvisor.MappingsProvider mappingsProvider;
    private final ProductMapper productMapper;

    public Result<List<PCCaseResponse>> findAll(Pageable pageable) {
        Page<PCCase> page = pcCaseRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
        ));
        return Result.success(pcCaseMapper.toResponseList(page.getContent()));
    }

    public Result<PCCaseResponse> findById(Long id) {
        return pcCaseRepository.findById(id)
                .map(p -> Result.success(pcCaseMapper.toResponse(p)))
                .orElse(Result.failure(ProductNotFound));
    }

    public Result<URI> create(PCCaseRequest dto, MultipartFile image,
                              HttpServletRequest request,
                              UriComponentsBuilder urb) {
        String imageUrl = productServices.saveImage(image, request);
        Result<PCCase> result = getCategoryAndBrand(dto);
        if (!result.isSuccess()) return Result.failure(result.getError());

        PCCase entity = result.getValue();
        entity.setImageUrl(imageUrl);
        PCCase saved = pcCaseRepository.save(entity);

        ProductEvent event = productMapper.toProductEvent(saved);
        event.setUrl("/pccases/" + saved.getId());
        productServices.publishEvent(event);

        URI uri = urb.path("/pccases/{id}").buildAndExpand(saved.getId()).toUri();
        return Result.success(uri);
    }

    public Result<Void> update(Long id, PCCaseRequest dto, MultipartFile image, HttpServletRequest request) {
        String imageUrl = (image.getSize() != 0) ? productServices.saveImage(image, request) : null;
        PCCase existing = pcCaseRepository.findById(id).orElse(null);
        if (existing == null) return Result.failure(ProductNotFound);

        Result<PCCase> result = getCategoryAndBrand(dto);
        if (!result.isSuccess()) return Result.failure(result.getError());

        PCCase updated = result.getValue();
        updated.setId(existing.getId());
        if (imageUrl != null) updated.setImageUrl(imageUrl);
        pcCaseRepository.save(updated);
        return Result.success();
    }

    public Result<Void> delete(Long id) {
        if (!pcCaseRepository.existsById(id)) return Result.failure(ProductNotFound);
        pcCaseRepository.deleteById(id);
        return Result.success();
    }

    private Result<PCCase> getCategoryAndBrand(PCCaseRequest dto) {
        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        Brand brand = brandRepository.findById(dto.getBrandId()).orElse(null);
        if (category == null) return Result.failure(CategoryNotFound);
        if (brand == null) return Result.failure(BrandNotFound);

        PCCase entity = pcCaseMapper.toEntity(dto);
        entity.setCategory(category);
        entity.setBrand(brand);
        return Result.success(entity);
    }
}
