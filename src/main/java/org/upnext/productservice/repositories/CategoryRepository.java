package org.upnext.productservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.upnext.productservice.entities.Category;

public interface CategoryRepository extends CrudRepository<Category, Long>, PagingAndSortingRepository<Category, Long> {
    Category findByName(String name);
}
