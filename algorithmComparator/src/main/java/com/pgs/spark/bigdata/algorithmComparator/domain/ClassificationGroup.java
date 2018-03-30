package com.pgs.spark.bigdata.algorithmComparator.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "classification_group")
public class ClassificationGroup extends AbstractEntity{

    @OneToOne
    @NotNull
    @JoinColumn(name="result_id")
    private Result result;

    @Enumerated(EnumType.STRING)
    @Column(name = "simpleClassification")
    private Classification simpleClassification;

    @Enumerated(EnumType.STRING)
    @Column(name = "crossClassification")
    private Classification crossValidatorClassification;

    @Enumerated(EnumType.STRING)
    @Column(name = "trainClassification")
    private Classification trainValidatorClassification;

    @Enumerated(EnumType.STRING)
    @Column(name = "multilayerClassification")
    private Classification multilayerPerceptronClassification;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Classification getSimpleClassification() {
        return simpleClassification;
    }

    public void setSimpleClassification(Classification simpleClassification) {
        this.simpleClassification = simpleClassification;
    }

    public Classification getCrossValidatorClassification() {
        return crossValidatorClassification;
    }

    public void setCrossValidatorClassification(Classification crossValidatorClassification) {
        this.crossValidatorClassification = crossValidatorClassification;
    }

    public Classification getTrainValidatorClassification() {
        return trainValidatorClassification;
    }

    public void setTrainValidatorClassification(Classification trainValidatorClassification) {
        this.trainValidatorClassification = trainValidatorClassification;
    }

    public Classification getMultilayerPerceptronClassification() {
        return multilayerPerceptronClassification;
    }

    public void setMultilayerPerceptronClassification(Classification multilayerPerceptronClassification) {
        this.multilayerPerceptronClassification = multilayerPerceptronClassification;
    }

}
