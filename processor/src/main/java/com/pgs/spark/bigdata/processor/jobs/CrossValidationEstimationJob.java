package com.pgs.spark.bigdata.processor.jobs;

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
        Tokenizer tokenizer = getTokenizer();
        HashingTF hashingTF = getHashingTF(tokenizer);

        LinearRegression lr = new LinearRegression()
                .setMaxIter(10)
                .setRegParam(0.01);
        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[] { tokenizer, hashingTF, lr });

        ParamMap[] paramGrid = new ParamGridBuilder()
                .addGrid(hashingTF.numFeatures(), numFeaturesTable)
                .addGrid(lr.maxIter(), maxIterTable)
                .addGrid(lr.regParam(), regParamTable)
                .build();

        CrossValidator crossValidator = new CrossValidator()
                .setEstimator(pipeline)
                .setEvaluator(new MulticlassClassificationEvaluator())
                .setEstimatorParamMaps(paramGrid)
                .setNumFolds(5);

        createModelAndSavePredictions(crossValidator, searchProfileId);
    }

}
