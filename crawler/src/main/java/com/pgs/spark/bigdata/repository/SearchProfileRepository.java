package com.pgs.spark.bigdata.repository;

import com.pgs.spark.bigdata.entities.SearchProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchProfileRepository extends CrudRepository<SearchProfile, Long> {
}
