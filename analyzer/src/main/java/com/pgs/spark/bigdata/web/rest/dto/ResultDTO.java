package com.pgs.spark.bigdata.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pgs.spark.bigdata.domain.enumeration.Classification;

/**
 * A DTO for the Result entity.
 */
public class ResultDTO implements Serializable {

    private Long id;

    private Classification classification;


    private Boolean isTrainingData;


    private Long documentId;
    private Long searchProfileId;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }
    public Boolean getIsTrainingData() {
        return isTrainingData;
    }

    public void setIsTrainingData(Boolean isTrainingData) {
        this.isTrainingData = isTrainingData;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }
    public Long getSearchProfileId() {
        return searchProfileId;
    }

    public void setSearchProfileId(Long searchProfileId) {
        this.searchProfileId = searchProfileId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResultDTO resultDTO = (ResultDTO) o;

        if ( ! Objects.equals(id, resultDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
            "id=" + id +
            ", classification='" + classification + "'" +
            ", isTrainingData='" + isTrainingData + "'" +
            '}';
    }
}
