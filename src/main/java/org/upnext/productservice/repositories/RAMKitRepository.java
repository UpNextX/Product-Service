package org.upnext.productservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.upnext.productservice.entities.RAMKit;

public interface RAMKitRepository extends CrudRepository<RAMKit, Long>, PagingAndSortingRepository<RAMKit, Long> {
    boolean existsById(Long id);
}
