package com.pgs.spark.bigdata.processor.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pgs.spark.bigdata.processor.domain.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    @Query("SELECT r FROM Result r WHERE r.searchProfile.id = :id AND r.isTrainingData = true")
    List<Result> findBySearchProfileToTraining(@Param("id") Long searchProfileId);

    @Query("SELECT r FROM Result r WHERE r.searchProfile.id = :id AND r.classification is null")
    Page<Result> findBySearchProfileToEstimating(@Param("id") Long searchProfileId, Pageable pageable);

    List<Result> findByDocumentIdAndSearchProfileId(Long documentId, Long id);
}

