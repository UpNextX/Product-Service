package org.upnext.productservice.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.upnext.productservice.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsById(Long id);
}
