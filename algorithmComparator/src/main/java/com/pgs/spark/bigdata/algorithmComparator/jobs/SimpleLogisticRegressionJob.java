package com.pgs.spark.bigdata.algorithmComparator.jobs;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.ml.regression.LinearRegression;
import org.springframework.stereotype.Component;

@Component
public class SimpleLogisticRegressionJob extends SparkJob {

    @Override
    public void run(final Long searchProfileId) {
        Tokenizer tokenizer = new Tokenizer()
                .setInputCol("text")
                .setOutputCol("words");
        HashingTF hashingTF = new HashingTF()
                .setNumFeatures(100)
                .setInputCol(tokenizer.getOutputCol())
                .setOutputCol("features");
        LinearRegression lr = new LinearRegression()
                .setMaxIter(10)
                .setRegParam(0.01);
        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[] {tokenizer, hashingTF, lr});
        createModelAndSavePredictions(pipeline, searchProfileId);
    }
}
