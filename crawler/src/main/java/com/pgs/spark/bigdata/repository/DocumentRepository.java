package com.pgs.spark.bigdata.repository;

import com.pgs.spark.bigdata.entities.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {

    @Query("SELECT d FROM Document d WHERE d.url = :url")
    Document findByUrl(@Param("url") String url);
}
