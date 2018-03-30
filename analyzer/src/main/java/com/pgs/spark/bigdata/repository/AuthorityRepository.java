package com.pgs.spark.bigdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgs.spark.bigdata.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
