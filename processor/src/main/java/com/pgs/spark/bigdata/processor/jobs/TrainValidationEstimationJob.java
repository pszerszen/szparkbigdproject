package com.pgs.spark.bigdata.processor.jobs;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.ml.tuning.TrainValidationSplit;
import org.springframework.stereotype.Component;

@Component
public class TrainValidationEstimationJob extends SparkJob {

    @Override
    public void run(final Long searchProfileId) {
        Tokenizer tokenizer = getTokenizer();
        HashingTF hashingTF = getHashingTF(tokenizer);

        LinearRegression lr = new LinearRegression();

        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[] { tokenizer, hashingTF, lr });

        ParamMap[] paramGrid = new ParamGridBuilder()
                .addGrid(hashingTF.numFeatures(), numFeaturesTable)
                .addGrid(lr.regParam(), regParamTable)
                .addGrid(lr.fitIntercept())
                .addGrid(lr.elasticNetParam(), elasticNetParamTable)
                .build();

        TrainValidationSplit trainValidationSplit = new TrainValidationSplit()
                .setEstimator(pipeline)
                .setEvaluator(new RegressionEvaluator())
                .setEstimatorParamMaps(paramGrid);

        createModelAndSavePredictions(trainValidationSplit, searchProfileId);
    }
}