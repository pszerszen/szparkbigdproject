package com.pgs.spark.bigdata.repository;

import com.pgs.spark.bigdata.domain.SearchProfile;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SearchProfile entity.
 */
public interface SearchProfileRepository extends JpaRepository<SearchProfile,Long> {

}
