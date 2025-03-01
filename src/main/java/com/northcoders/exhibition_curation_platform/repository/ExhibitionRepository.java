package com.northcoders.exhibition_curation_platform.repository;

import com.northcoders.exhibition_curation_platform.model.Exhibition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends CrudRepository<Exhibition, Long> {
}
