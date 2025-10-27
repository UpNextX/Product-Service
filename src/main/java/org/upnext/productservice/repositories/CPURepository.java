package org.upnext.productservice.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.upnext.productservice.entities.CPU;

public interface CPURepository extends CrudRepository<CPU, Long>, PagingAndSortingRepository<CPU, Long> {
    boolean existsById(Long id);
}
