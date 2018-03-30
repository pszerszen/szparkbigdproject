package com.pgs.spark.bigdata.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.pgs.spark.bigdata.domain.enumeration.Classification;

/**
 * A Result.
 */
@Entity
@Table(name = "result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "classification")
    private Classification classification;

    @Column(name = "is_training_data")
    private Boolean isTrainingData;

    @ManyToOne
    private Document document;

    @ManyToOne
    private SearchProfile searchProfile;

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

    public Boolean isIsTrainingData() {
        return isTrainingData;
    }

    public void setIsTrainingData(Boolean isTrainingData) {
        this.isTrainingData = isTrainingData;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public SearchProfile getSearchProfile() {
        return searchProfile;
    }

    public void setSearchProfile(SearchProfile searchProfile) {
        this.searchProfile = searchProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        final Result result = (Result) o;
        if((result.id == null) || (id == null)) {
            return false;
        }
        return Objects.equals(id, result.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Result{" +
            "id=" + id +
            ", classification='" + classification + "'" +
            ", isTrainingData='" + isTrainingData + "'" +
            '}';
    }
}
