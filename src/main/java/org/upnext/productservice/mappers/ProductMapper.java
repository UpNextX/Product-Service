package org.upnext.productservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.upnext.productservice.contracts.products.ProductRequest;
import org.upnext.productservice.contracts.products.ProductResponse;
import org.upnext.productservice.entities.Product;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toProduct(ProductRequest productRequest);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "brand.id", target = "brandId")
    ProductResponse toProductResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> products);
}
