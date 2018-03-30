package com.pgs.spark.bigdata.algorithmComparator.repository;

import com.pgs.spark.bigdata.algorithmComparator.domain.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    @Query("SELECT r FROM Result r WHERE r.searchProfile.id = :id AND r.isTrainingData = true")
    List<Result> findBySearchProfileToTraining(@Param("id") Long searchProfileId);

/*    @Query("SELECT r FROM Result r WHERE r.searchProfile.id = :id AND r.classification is null")
    Page<Result> findBySearchProfileToEstimating(@Param("id") Long searchProfileId, Pageable pageable);*/

    @Query("SELECT r FROM ClassificationGroup cg RIGHT JOIN cg.result r WHERE r.searchProfile.id = :id " +
            "AND cg.simpleClassification is null OR cg.crossValidatorClassification is null OR cg.trainValidatorClassification is null OR cg.multilayerPerceptronClassification is null")
    Page<Result> findBySearchProfileToEstimating(@Param("id") Long searchProfileId, Pageable pageable);

    List<Result> findByDocumentIdAndSearchProfileId(Long documentId, Long id);
}

