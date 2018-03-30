package com.pgs.spark.bigdata.algorithmComparator.jobs;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.tuning.CrossValidator;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.springframework.stereotype.Component;

@Component
public class CrossValidationEstimationJob extends SparkJob {

    @Override
    public void run(final Long searchProfileId) {
        Tokenizer tokenizer = new Tokenizer()
                .setInputCol("text")
                .setOutputCol("words");
        HashingTF hashingTF = new HashingTF()
                .setNumFeatures(getMaxFeatures())
                .setInputCol(tokenizer.getOutputCol())
                .setOutputCol("features");

        LinearRegression lr = new LinearRegression()
                .setMaxIter(10)
                .setRegParam(0.01);
        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[] { tokenizer, hashingTF, lr });

        ParamMap[] paramGrid = new ParamGridBuilder()
                .addGrid(hashingTF.numFeatures(), new int[] { 1, 10, 100 })
                .addGrid(lr.maxIter(), new int[] { 10, 50, 100 })
                .addGrid(lr.regParam(), new double[] { 0.1, 0.01 })
                .build();

        CrossValidator crossValidator = new CrossValidator()
                .setEstimator(pipeline)
                .setEvaluator(new MulticlassClassificationEvaluator())
                .setEstimatorParamMaps(paramGrid)
                .setNumFolds(5);

        createModelAndSavePredictions(crossValidator, searchProfileId);
    }

}
