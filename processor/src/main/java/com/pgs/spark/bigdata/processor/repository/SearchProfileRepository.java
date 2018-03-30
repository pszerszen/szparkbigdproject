package com.pgs.spark.bigdata.processor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgs.spark.bigdata.processor.domain.SearchProfile;

@Repository
public interface SearchProfileRepository extends JpaRepository<SearchProfile, Long> {
}
