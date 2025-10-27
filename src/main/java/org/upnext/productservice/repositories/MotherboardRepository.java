package org.upnext.productservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.upnext.productservice.entities.Motherboard;

public interface MotherboardRepository extends CrudRepository<Motherboard, Long>, PagingAndSortingRepository<Motherboard, Long> {
    boolean existsById(Long id);
}
