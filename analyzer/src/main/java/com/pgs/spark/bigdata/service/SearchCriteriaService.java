package com.pgs.spark.bigdata.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgs.spark.bigdata.domain.SearchCriteria;
import com.pgs.spark.bigdata.domain.SearchProfile;
import com.pgs.spark.bigdata.repository.SearchCriteriaRepository;
import com.pgs.spark.bigdata.web.rest.dto.ResultDTO;
import com.pgs.spark.bigdata.web.rest.dto.SearchCriteriaDTO;
import com.pgs.spark.bigdata.web.rest.mapper.SearchCriteriaMapper;

/**
 * Service Implementation for managing SearchCriteria.
 */
@Service
@Transactional
public class SearchCriteriaService {

    private final Logger log = LoggerFactory.getLogger(SearchCriteriaService.class);
    
    @Inject
    private SearchCriteriaRepository searchCriteriaRepository;
    
    @Inject
    private SearchCriteriaMapper searchCriteriaMapper;
    
    @Inject
    private ResultService resultService;
    
    /**
     * Save a searchCriteria.
     * 
     * @param searchCriteriaDTO the entity to save
     * @return the persisted entity
     */
    public SearchCriteriaDTO save(SearchCriteriaDTO searchCriteriaDTO) {
        log.debug("Request to save SearchCriteria : {}", searchCriteriaDTO);
        SearchCriteria searchCriteria = searchCriteriaMapper.searchCriteriaDTOToSearchCriteria(searchCriteriaDTO);
        cleanResults(searchCriteria);
        searchCriteria = searchCriteriaRepository.save(searchCriteria);
        final SearchCriteriaDTO result = searchCriteriaMapper.searchCriteriaToSearchCriteriaDTO(searchCriteria);
        return result;
    }

    private void cleanResults(SearchCriteria searchCriteria) {
		final SearchProfile searchProfile = searchCriteria.getSearchProfile();
		final List<ResultDTO> resultDTOs = resultService.findBySearchProfile(searchProfile);
		resultDTOs.forEach(r -> resultService.delete(r.getId()));
	}

	/**
     *  Get all the searchCriteria.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<SearchCriteriaDTO> findAll() {
        log.debug("Request to get all SearchCriteria");
        final List<SearchCriteriaDTO> result = searchCriteriaRepository.findAll().stream()
            .map(searchCriteriaMapper::searchCriteriaToSearchCriteriaDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get one searchCriteria by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SearchCriteriaDTO findOne(Long id) {
        log.debug("Request to get SearchCriteria : {}", id);
        final SearchCriteria searchCriteria = searchCriteriaRepository.findOne(id);
        final SearchCriteriaDTO searchCriteriaDTO = searchCriteriaMapper.searchCriteriaToSearchCriteriaDTO(searchCriteria);
        return searchCriteriaDTO;
    }

    /**
     *  Delete the  searchCriteria by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SearchCriteria : {}", id);
        searchCriteriaRepository.delete(id);
    }
}
