package org.upnext.productservice.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.commons.config.DefaultsBindHandlerAdvisor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.contracts.gpus.GPURequest;
import org.upnext.productservice.contracts.gpus.GPUResponse;
import org.upnext.productservice.entities.Brand;
import org.upnext.productservice.entities.Category;
import org.upnext.productservice.entities.GPU;
import org.upnext.productservice.mappers.GPUMapper;
import org.upnext.productservice.mappers.ProductMapper;
import org.upnext.productservice.repositories.BrandRepository;
import org.upnext.productservice.repositories.CategoryRepository;
import org.upnext.productservice.repositories.GPURepository;
import org.upnext.sharedlibrary.Errors.Result;
import org.upnext.sharedlibrary.Events.ProductEvent;

import java.net.URI;
import java.util.List;

import static org.upnext.productservice.errors.BrandErrors.BrandNotFound;
import static org.upnext.productservice.errors.CategoryErrors.CategoryNotFound;
import static org.upnext.productservice.errors.ProductErrors.ProductNotFound;

@Service
@RequiredArgsConstructor
public class GPUServices {

    private final GPURepository gpuRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final GPUMapper gpuMapper;
    private final ProductServices productServices;
    private final DefaultsBindHandlerAdvisor.MappingsProvider mappingsProvider;
    private final ProductMapper productMapper;

    public Result<List<GPUResponse>> findAll(Pageable pageable) {
        Page<GPU> page = gpuRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
        ));
        return Result.success(gpuMapper.toResponseList(page.getContent()));
    }

    public Result<GPUResponse> findById(Long id) {
        return gpuRepository.findById(id)
                .map(gpu -> Result.success(gpuMapper.toResponse(gpu)))
                .orElse(Result.failure(ProductNotFound));
    }

    public Result<URI> create(GPURequest dto, MultipartFile image,
                              HttpServletRequest request,
                              UriComponentsBuilder urb) {
        String imageUrl = productServices.saveImage(image, request);
        Result<GPU> result = getCategoryAndBrand(dto);
        if (!result.isSuccess()) return Result.failure(result.getError());

        GPU gpu = result.getValue();
        gpu.setImageUrl(imageUrl);
        GPU saved = gpuRepository.save(gpu);

        ProductEvent event = productMapper.toProductEvent(saved);
        event.setUrl("http://localhost:5173/gpus/" + saved.getId());
        productServices.publishEvent(event);

        URI uri = urb.path("/gpus/{id}").buildAndExpand(saved.getId()).toUri();
        return Result.success(uri);
    }

    public Result<Void> update(Long id, GPURequest dto, MultipartFile image, HttpServletRequest request) {
        String imageUrl = (image.getSize() != 0) ? productServices.saveImage(image, request) : null;
        GPU existing = gpuRepository.findById(id).orElse(null);
        if (existing == null) return Result.failure(ProductNotFound);

        Result<GPU> result = getCategoryAndBrand(dto);
        if (!result.isSuccess()) return Result.failure(result.getError());

        GPU updated = result.getValue();
        updated.setId(existing.getId());
        if (imageUrl != null) updated.setImageUrl(imageUrl);
        gpuRepository.save(updated);
        return Result.success();
    }

    public Result<Void> delete(Long id) {
        if (!gpuRepository.existsById(id)) return Result.failure(ProductNotFound);
        gpuRepository.deleteById(id);
        return Result.success();
    }

    private Result<GPU> getCategoryAndBrand(GPURequest dto) {
        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        Brand brand = brandRepository.findById(dto.getBrandId()).orElse(null);
        if (category == null) return Result.failure(CategoryNotFound);
        if (brand == null) return Result.failure(BrandNotFound);

        GPU gpu = gpuMapper.toEntity(dto);
        gpu.setCategory(category);
        gpu.setBrand(brand);
        return Result.success(gpu);
    }
}
