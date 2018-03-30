package com.pgs.spark.bigdata.web.rest.mapper;

import com.pgs.spark.bigdata.domain.*;
import com.pgs.spark.bigdata.web.rest.dto.ResultDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Result and its DTO ResultDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ResultMapper {

    @Mapping(source = "document.id", target = "documentId")
    @Mapping(source = "searchProfile.id", target = "searchProfileId")
    ResultDTO resultToResultDTO(Result result);

    List<ResultDTO> resultsToResultDTOs(List<Result> results);

    @Mapping(source = "documentId", target = "document")
    @Mapping(source = "searchProfileId", target = "searchProfile")
    Result resultDTOToResult(ResultDTO resultDTO);

    List<Result> resultDTOsToResults(List<ResultDTO> resultDTOs);

    default Document documentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Document document = new Document();
        document.setId(id);
        return document;
    }

    default SearchProfile searchProfileFromId(Long id) {
        if (id == null) {
            return null;
        }
        SearchProfile searchProfile = new SearchProfile();
        searchProfile.setId(id);
        return searchProfile;
    }
}
