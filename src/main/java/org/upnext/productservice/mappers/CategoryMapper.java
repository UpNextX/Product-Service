package org.upnext.productservice.mappers;


import org.mapstruct.Mapper;
import org.upnext.productservice.contracts.categories.*;
import org.upnext.productservice.entities.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest categoryRequest);

    CategoryResponse toCategoryResponse(Category category);

    List<CategoryResponse> toCategoryResponseList(List<Category> categories);
}
