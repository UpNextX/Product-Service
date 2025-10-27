package org.upnext.productservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.upnext.productservice.entities.GPU;

public interface GPURepository extends CrudRepository<GPU, Long>, PagingAndSortingRepository<GPU, Long> {
    boolean existsById(Long id);
}
