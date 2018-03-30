package com.pgs.spark.bigdata.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pgs.spark.bigdata.domain.Result;
import com.pgs.spark.bigdata.domain.SearchProfile;
import com.pgs.spark.bigdata.web.rest.dto.ChartDataDTO;
import com.pgs.spark.bigdata.web.rest.dto.ChartScale;
import com.pgs.spark.bigdata.web.rest.dto.ResultDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Service Interface for managing Result.
 */
public interface ResultService {

    /**
     * Save a result.
     *
     * @param resultDTO the entity to save
     * @return the persisted entity
     */
    ResultDTO save(ResultDTO resultDTO);

    /**
     *  Get all the results.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Result> findAll(Pageable pageable);

    /**
     *  Get the "id" result.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ResultDTO findOne(Long id);

    /**
     *  Delete the "id" result.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * find results for given search Profile
     *
     * @param searchProfile searched profile for searched result;
     */
	List<ResultDTO> findBySearchProfile(SearchProfile searchProfile);

    ChartDataDTO prepareChartData(final Long searchProfile, final LocalDate from, final LocalDate until, final ChartScale scale);
}
