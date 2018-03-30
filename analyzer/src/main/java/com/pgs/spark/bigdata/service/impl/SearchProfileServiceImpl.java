package com.pgs.spark.bigdata.service.impl;

import com.pgs.spark.bigdata.service.SearchProfileService;
import com.pgs.spark.bigdata.domain.SearchProfile;
import com.pgs.spark.bigdata.repository.SearchProfileRepository;
import com.pgs.spark.bigdata.web.rest.dto.SearchProfileDTO;
import com.pgs.spark.bigdata.web.rest.mapper.SearchProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SearchProfile.
 */
@Service
@Transactional
public class SearchProfileServiceImpl implements SearchProfileService{

    private final Logger log = LoggerFactory.getLogger(SearchProfileServiceImpl.class);
    
    @Inject
    private SearchProfileRepository searchProfileRepository;
    
    @Inject
    private SearchProfileMapper searchProfileMapper;
    
    /**
     * Save a searchProfile.
     * 
     * @param searchProfileDTO the entity to save
     * @return the persisted entity
     */
    public SearchProfileDTO save(SearchProfileDTO searchProfileDTO) {
        log.debug("Request to save SearchProfile : {}", searchProfileDTO);
        SearchProfile searchProfile = searchProfileMapper.searchProfileDTOToSearchProfile(searchProfileDTO);
        searchProfile = searchProfileRepository.save(searchProfile);
        SearchProfileDTO result = searchProfileMapper.searchProfileToSearchProfileDTO(searchProfile);
        return result;
    }

    /**
     *  Get all the searchProfiles.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<SearchProfileDTO> findAll() {
        log.debug("Request to get all SearchProfiles");
        List<SearchProfileDTO> result = searchProfileRepository.findAll().stream()
            .map(searchProfileMapper::searchProfileToSearchProfileDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get one searchProfile by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SearchProfileDTO findOne(Long id) {
        log.debug("Request to get SearchProfile : {}", id);
        SearchProfile searchProfile = searchProfileRepository.findOne(id);
        SearchProfileDTO searchProfileDTO = searchProfileMapper.searchProfileToSearchProfileDTO(searchProfile);
        return searchProfileDTO;
    }

    /**
     *  Delete the  searchProfile by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SearchProfile : {}", id);
        searchProfileRepository.delete(id);
    }
}
