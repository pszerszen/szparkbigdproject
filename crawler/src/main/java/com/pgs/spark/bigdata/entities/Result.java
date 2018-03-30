package com.pgs.spark.bigdata.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "result")
public class Result extends AbstractEntity {
    private static final long serialVersionUID = -7388792031944038844L;

    @Column(name = "classification", columnDefinition = "VARCHAR(45)")
    private String classification;

    @Column(name = "isTrainingData", nullable = false)
    private boolean isTrainingData;

    @ManyToOne(optional = false)
    @JoinColumn(name = "document_id")
    private Document document;

    @ManyToOne(optional = false)
    @JoinColumn(name = "searchProfile_id")
    private SearchProfile searchProfile;

    public String getClassification() {
        return classification;
    }

    public void setClassification(final String classification) {
        this.classification = classification;
    }

    public boolean isTrainingData() {
        return isTrainingData;
    }

    public void setTrainingData(final boolean trainingData) {
        isTrainingData = trainingData;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(final Document document) {
        this.document = document;
    }

    public SearchProfile getSearchProfile() {
        return searchProfile;
    }

    public void setSearchProfile(final SearchProfile searchProfile) {
        this.searchProfile = searchProfile;
    }
}
