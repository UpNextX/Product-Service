package org.upnext.productservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.upnext.productservice.entities.PSU;

public interface PSURepository extends CrudRepository<PSU, Long>, PagingAndSortingRepository<PSU, Long> {
    boolean existsById(Long id);
}
