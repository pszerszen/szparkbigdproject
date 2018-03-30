package com.pgs.spark.bigdata.repository;

import com.pgs.spark.bigdata.domain.Result;
import com.pgs.spark.bigdata.domain.SearchProfile;
import com.pgs.spark.bigdata.domain.enumeration.Classification;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Result entity.
 */
public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findBySearchProfile(SearchProfile searchProfile);

    /**
     * Creates a juxtaposition of {@link LocalDate}s and numbers of documents for specified search profile, classification and time range.
     * @param searchProfileId id of search profile
     * @param type a classification
     * @param from beginning of the time range
     * @param until end of the time range
     * @return List of {@link Object} arrays, can be converted into {@link List}&lt;{@link Pair}&lt;{@link LocalDate},{@link Long}&gt;&gt; objects.
     */
    @Query("SELECT r.document.updateDate, COUNT(r) FROM Result r " +
        "WHERE r.searchProfile.id = :searchProfile " +
        "   AND r.classification = :classification " +
        "   AND r.document.updateDate BETWEEN :fromDate AND :untilDate " +
        "GROUP BY r.document.updateDate")
    List<Object[]> findResultsBySearchProfileAndClassificationInRange(@Param("searchProfile") Long searchProfileId,
                                                                    @Param("classification") Classification type,
                                                                    @Param("fromDate") LocalDate from,
                                                                    @Param("untilDate") LocalDate until);
}
