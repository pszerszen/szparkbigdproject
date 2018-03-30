package com.pgs.spark.bigdata.web.rest.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pgs.spark.bigdata.domain.SearchCriteria;
import com.pgs.spark.bigdata.domain.SearchProfile;
import com.pgs.spark.bigdata.web.rest.dto.SearchCriteriaDTO;

/**
 * Mapper for the entity SearchCriteria and its DTO SearchCriteriaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SearchCriteriaMapper {

    @Mapping(source = "searchProfile.id", target = "searchProfileId")
    SearchCriteriaDTO searchCriteriaToSearchCriteriaDTO(SearchCriteria searchCriteria);

    List<SearchCriteriaDTO> searchCriteriaToSearchCriteriaDTOs(List<SearchCriteria> searchCriteria);

    @Mapping(source = "searchProfileId", target = "searchProfile")
    SearchCriteria searchCriteriaDTOToSearchCriteria(SearchCriteriaDTO searchCriteriaDTO);

    List<SearchCriteria> searchCriteriaDTOsToSearchCriteria(List<SearchCriteriaDTO> searchCriteriaDTOs);

    default SearchProfile searchProfileFromId(Long id) {
        if (id == null) {
            return null;
        }
        SearchProfile searchProfile = new SearchProfile();
        searchProfile.setId(id);
        return searchProfile;
    }
}
