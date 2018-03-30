package com.pgs.spark.bigdata.repository;

import com.pgs.spark.bigdata.entities.SearchCriteria;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchCriteriaRepository extends CrudRepository<SearchCriteria, Long> {
}
