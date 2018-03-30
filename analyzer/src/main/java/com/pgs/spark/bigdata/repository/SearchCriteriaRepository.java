package com.pgs.spark.bigdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgs.spark.bigdata.domain.SearchCriteria;

/**
 * Spring Data JPA repository for the SearchCriteria entity.
 */
public interface SearchCriteriaRepository extends JpaRepository<SearchCriteria,Long> {

}
