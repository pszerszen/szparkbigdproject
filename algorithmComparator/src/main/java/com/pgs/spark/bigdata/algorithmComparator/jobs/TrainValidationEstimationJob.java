package com.pgs.spark.bigdata.algorithmComparator.jobs;

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
        Tokenizer tokenizer = new Tokenizer()
                .setInputCol("text")
                .setOutputCol("words");
        HashingTF hashingTF = new HashingTF()
                .setNumFeatures(getMaxFeatures())
                .setInputCol(tokenizer.getOutputCol())
                .setOutputCol("features");

        LinearRegression lr = new LinearRegression();

        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[] { tokenizer, hashingTF, lr });

        ParamMap[] paramGrid = new ParamGridBuilder()
                .addGrid(hashingTF.numFeatures(), new int[] { 1, 10, 100 })
                .addGrid(lr.regParam(), new double[] { 0.1, 0.01 })
                .addGrid(lr.fitIntercept())
                .addGrid(lr.elasticNetParam(), new double[] {0.0, 0.5, 1.0})
                .build();

        TrainValidationSplit trainValidationSplit = new TrainValidationSplit()
                .setEstimator(pipeline)
                .setEvaluator(new RegressionEvaluator())
                .setEstimatorParamMaps(paramGrid);

        createModelAndSavePredictions(trainValidationSplit, searchProfileId);

    }
}
