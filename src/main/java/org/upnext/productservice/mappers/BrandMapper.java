package org.upnext.productservice.mappers;

import org.mapstruct.Mapper;
import org.upnext.productservice.contracts.brands.BrandRequest;
import org.upnext.productservice.contracts.brands.BrandResponse;
import org.upnext.productservice.entities.Brand;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toBrand(BrandRequest BrandRequest);

    BrandResponse toBrandResponse(Brand Brand);

    List<BrandResponse> toBrandResponseList(List<Brand> brands);
}
