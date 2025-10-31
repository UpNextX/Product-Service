package org.upnext.productservice.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.commons.config.DefaultsBindHandlerAdvisor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.contracts.cpus.CPURequest;
import org.upnext.productservice.contracts.cpus.CPUResponse;
import org.upnext.productservice.contracts.products.ProductRequest;
import org.upnext.productservice.entities.Brand;
import org.upnext.productservice.entities.Category;
import org.upnext.productservice.entities.CPU;
import org.upnext.productservice.entities.Product;
import org.upnext.productservice.mappers.ProductMapper;
import org.upnext.productservice.repositories.BrandRepository;
import org.upnext.productservice.repositories.CategoryRepository;
import org.upnext.productservice.repositories.CPURepository;
import org.upnext.sharedlibrary.Errors.Result;
import org.upnext.productservice.mappers.CPUMapper;
import org.upnext.sharedlibrary.Events.ProductEvent;

import java.net.URI;
import java.util.List;

import static org.upnext.productservice.errors.BrandErrors.BrandNotFound;
import static org.upnext.productservice.errors.CategoryErrors.CategoryNotFound;
import static org.upnext.productservice.errors.ProductErrors.ProductNotFound;

@Service
@RequiredArgsConstructor
public class CPUServices {

    private final CPURepository cpuRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final CPUMapper cpuMapper;
    private final ProductServices productServices;
    private final DefaultsBindHandlerAdvisor.MappingsProvider mappingsProvider;
    private final ProductMapper productMapper;

    public Result<List<CPUResponse>> findAll(Pageable pageable) {
        Page<CPU> page = cpuRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
        ));

        List<CPUResponse> responses = cpuMapper.toResponseList(page.getContent());
        return Result.success(responses);
    }

    public Result<CPUResponse> findById(Long id) {
        return cpuRepository.findById(id)
                .map(cpu -> Result.success(cpuMapper.toResponse(cpu)))
                .orElse(Result.failure(ProductNotFound));
    }

    public Result<URI> create(CPURequest cpuDto, MultipartFile image,
                                      HttpServletRequest request,
                                      UriComponentsBuilder urb) {
        String imageUrl = productServices.saveImage(image, request);
        Category category = categoryRepository.findById(cpuDto.getCategoryId()).orElse(null);
        Result<CPU> result = getCategoryAndBrand(cpuDto);
        if (!result.isSuccess()) {
            return Result.failure(result.getError());
        }

        CPU cpu = result.getValue();
        cpu.setImageUrl(imageUrl);
        CPU cpu1 = cpuRepository.save(cpu);

        ProductEvent productEvent = productMapper.toProductEvent(cpu1);
        productEvent.setUrl("http://localhost:5173/cpus/" + cpu1.getId());
        productServices.publishEvent(productEvent);

        URI uri = urb
                .path("/cpus/{id}")
                .buildAndExpand(cpu1.getId())
                .toUri();
        return Result.success(uri);
    }

    public Result<Void> update(Long id, CPURequest cpuDto, MultipartFile image, HttpServletRequest request) {
        String imageUrl = null;
        if (image.getSize() != 0) {
            imageUrl = productServices.saveImage(image, request);
        }
        CPU cpu = cpuRepository.findById(id).orElse(null);
        if (cpu == null) {
            return Result.failure(ProductNotFound);
        }


        Result<CPU> result = getCategoryAndBrand(cpuDto);
        if (!result.isSuccess()) {
            return Result.failure(result.getError());
        }
        CPU cpu1 = result.getValue();

        cpu1.setId(cpu.getId());
        if (imageUrl != null) {
            cpu1.setImageUrl(imageUrl);
        }
        cpuRepository.save(cpu1);
        return Result.success();
    }

    public Result<Void> delete(Long id) {
        if (!cpuRepository.existsById(id)) {
            return Result.failure(ProductNotFound);
        }
        cpuRepository.deleteById(id);
        return Result.success();
    }


    private Result<CPU> getCategoryAndBrand(CPURequest cpuDto) {
        Category category = categoryRepository.findById(cpuDto.getCategoryId()).orElse(null);
        Brand brand = brandRepository.findById(cpuDto.getBrandId()).orElse(null);
        if (category == null ) {
            return Result.failure(CategoryNotFound);
        }
        if (brand == null ) {
            return Result.failure(BrandNotFound);
        }

        CPU cpu = cpuMapper.toEntity(cpuDto);

        cpu.setCategory(category);
        cpu.setBrand(brand);
        return Result.success(cpu);
    }

}
