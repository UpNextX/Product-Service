package org.upnext.productservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.upnext.productservice.entities.Brand;

public interface BrandRepository extends CrudRepository<Brand, Long> , PagingAndSortingRepository<Brand, Long> {
    Brand findByName(String name);
}
