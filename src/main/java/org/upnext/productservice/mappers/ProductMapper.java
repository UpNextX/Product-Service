package org.upnext.productservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.upnext.productservice.contracts.products.ProductRequest;
import org.upnext.productservice.contracts.products.ProductResponse;
import org.upnext.productservice.entities.Product;
import org.upnext.sharedlibrary.Events.ProductEvent;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toProduct(ProductRequest productRequest);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "brand.name", target = "brandName")
    ProductResponse toProductResponse(Product product);

    @Mapping(source = "category.name", target = "category")
    @Mapping(source = "brand.name", target = "brand")
    @Mapping(source = "name", target = "name")
    ProductEvent  toProductEvent(Product product);
    List<ProductResponse> toProductResponseList(List<Product> products);
}
