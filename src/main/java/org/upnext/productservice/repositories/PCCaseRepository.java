package org.upnext.productservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.upnext.productservice.entities.PCCase;

public interface PCCaseRepository extends CrudRepository<PCCase, Long>, PagingAndSortingRepository<PCCase, Long> {
    boolean existsById(Long id);
}
