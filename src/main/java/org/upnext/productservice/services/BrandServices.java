package org.upnext.productservice.services;


import lombok.RequiredArgsConstructor;
import org.springframework.cloud.commons.config.DefaultsBindHandlerAdvisor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.contracts.brands.BrandRequest;
import org.upnext.productservice.contracts.brands.BrandResponse;
import org.upnext.productservice.entities.Brand;
import org.upnext.productservice.mappers.BrandMapper;
import org.upnext.productservice.repositories.BrandRepository;
import org.upnext.sharedlibrary.Errors.Result;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static org.upnext.productservice.errors.BrandErrors.*;

@Service
@RequiredArgsConstructor
public class BrandServices {

    public final BrandRepository BrandRepository;
    public final BrandMapper  BrandMapper;


    public Result<BrandResponse> findBrandById(Long id) {
        Brand Brand = findByBrandId(id);

        if (Brand == null) {
            return Result.failure(BrandNotFound);
        }

        return Result.success(BrandMapper.toBrandResponse(Brand));
    }


    public Result<List<BrandResponse>> findAllBrands(Pageable pageable) {
        Page<Brand> page = BrandRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                )
        );
        return Result.success(BrandMapper.toBrandResponseList(page.getContent()));
    }


    public Result<BrandResponse> findBrandByName(String name) {
        Brand Brand = findByName(name);

        if (Brand == null) {
            return Result.failure(BrandNotFound);
        }

        return Result.success(BrandMapper.toBrandResponse(Brand));
    }


    public Result<URI> createNewBrand(BrandRequest BrandRequest, UriComponentsBuilder urb) {

        Brand Brand = findByName(BrandRequest.getName());
        if (Brand != null) {
            return Result.failure(DuplicatedBrandName);
        }

        Brand = BrandMapper.toBrand(BrandRequest);
        Brand = BrandRepository.save(Brand);

        URI uri = urb.path("/brands/{id}")
                .buildAndExpand(Brand.getId())
                .toUri();
        return Result.success(uri);
    }

    public Result<Void> updateBrand(Long id, BrandRequest BrandRequest) {
        Brand Brand = findByBrandId(id);
        if  (Brand == null) {
            return Result.failure(BrandNotFound);
        }

        Brand = findByName(BrandRequest.getName());

        if (Brand != null && !Objects.equals(id, Brand.getId())) {
            return Result.failure(DuplicatedBrandName);
        }

        Brand = BrandMapper.toBrand(BrandRequest);
        Brand.setId(id);
        BrandRepository.save(Brand);

        return  Result.success();
    }

    public Result<Void> deleteBrand(Long id) {
        Brand Brand = findByBrandId(id);
        if (Brand == null) {
            return Result.failure(BrandNotFound);
        }
        BrandRepository.delete(Brand);
        return Result.success();
    }

    private Brand findByName(String name) {
        return BrandRepository.findByName(name);
    }

    private Brand findByBrandId(Long id) {
        return BrandRepository.findById(id).orElse(null);
    }

}
