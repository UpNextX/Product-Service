package org.upnext.productservice.mappers;


import org.mapstruct.Mapping;
import java.util.List;


public interface AbstractProductMapper<T, RQ, RS> {

    T toEntity(RQ request);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "brand.name", target = "brandName")
    RS toResponse(T entity);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "brand.name", target = "brandName")
    List<RS> toResponseList(List<T> entities);
}