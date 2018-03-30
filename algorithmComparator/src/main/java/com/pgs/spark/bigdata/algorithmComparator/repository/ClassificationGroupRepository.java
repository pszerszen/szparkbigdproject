package com.pgs.spark.bigdata.algorithmComparator.repository;

import com.pgs.spark.bigdata.algorithmComparator.domain.Classification;
import com.pgs.spark.bigdata.algorithmComparator.domain.ClassificationGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ClassificationGroupRepository extends JpaRepository<ClassificationGroup, Long> {

    Optional<ClassificationGroup> findByResultId(Long resultId);

    @Query("from ClassificationGroup cg where cg.result.searchProfile.id = :searchProfileId")
    List<ClassificationGroup> findBySearchProfileId(@Param("searchProfileId") Long searchProfileId);

    @Query("SELECT cg.result.document.updateDate, COUNT(cg) FROM ClassificationGroup cg " +
            "WHERE cg.result.searchProfile.id = :searchProfile " +
            "   AND cg.simpleClassification = :classification " +
            "   AND cg.result.document.updateDate BETWEEN :fromDate AND :untilDate " +
            "GROUP BY cg.result.document.updateDate")
    List<Object[]> findResultsBySearchProfileAndSimpleClassificationInRange(@Param("searchProfile") Long searchProfileId,
                                                                      @Param("classification") Classification type,
                                                                      @Param("fromDate") Date from,
                                                                      @Param("untilDate") Date until);

    @Query("SELECT cg.result.document.updateDate, COUNT(cg) FROM ClassificationGroup cg " +
            "WHERE cg.result.searchProfile.id = :searchProfile " +
            "   AND cg.crossValidatorClassification = :classification " +
            "   AND cg.result.document.updateDate BETWEEN :fromDate AND :untilDate " +
            "GROUP BY cg.result.document.updateDate")
    List<Object[]> findResultsBySearchProfileAndCrossClassificationInRange(@Param("searchProfile") Long searchProfileId,
                                                                            @Param("classification") Classification type,
                                                                            @Param("fromDate") Date from,
                                                                            @Param("untilDate") Date until);


    @Query("SELECT cg.result.document.updateDate, COUNT(cg) FROM ClassificationGroup cg " +
            "WHERE cg.result.searchProfile.id = :searchProfile " +
            "   AND cg.multilayerPerceptronClassification = :classification " +
            "   AND cg.result.document.updateDate BETWEEN :fromDate AND :untilDate " +
            "GROUP BY cg.result.document.updateDate")
    List<Object[]> findResultsBySearchProfileAndMultilayerClassificationInRange(@Param("searchProfile") Long searchProfileId,
                                                                            @Param("classification") Classification type,
                                                                            @Param("fromDate") Date from,
                                                                            @Param("untilDate") Date until);

    @Query("SELECT cg.result.document.updateDate, COUNT(cg) FROM ClassificationGroup cg " +
            "WHERE cg.result.searchProfile.id = :searchProfile " +
            "   AND cg.trainValidatorClassification = :classification " +
            "   AND cg.result.document.updateDate BETWEEN :fromDate AND :untilDate " +
            "GROUP BY cg.result.document.updateDate")
    List<Object[]> findResultsBySearchProfileAndTrainClassificationInRange(@Param("searchProfile") Long searchProfileId,
                                                                            @Param("classification") Classification type,
                                                                            @Param("fromDate") Date from,
                                                                            @Param("untilDate") Date until);
}
