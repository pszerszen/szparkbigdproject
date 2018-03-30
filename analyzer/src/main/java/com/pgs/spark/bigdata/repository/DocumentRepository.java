package com.pgs.spark.bigdata.repository;

import com.pgs.spark.bigdata.domain.Document;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Document entity.
 */
public interface DocumentRepository extends JpaRepository<Document,Long> {

}
