package com.pgs.spark.bigdata.repository;

import com.pgs.spark.bigdata.entities.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends CrudRepository<Result, Long> {
}
