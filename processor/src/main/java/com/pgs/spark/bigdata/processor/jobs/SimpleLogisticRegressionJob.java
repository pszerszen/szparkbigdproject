package com.pgs.spark.bigdata.processor.jobs;

import com.pgs.spark.bigdata.processor.config.AppConfig;
import com.pgs.spark.bigdata.processor.config.DbConfig;
import com.pgs.spark.bigdata.processor.repository.SearchProfileRepository;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.ml.regression.LinearRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleLogisticRegressionJob extends SparkJob {

    public void run(final Long searchProfileId) {
        Tokenizer tokenizer = getTokenizer();
        HashingTF hashingTF = getHashingTF(tokenizer);

        LinearRegression lr = new LinearRegression()
                .setMaxIter(10)
                .setRegParam(0.01);
        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[] {tokenizer, hashingTF, lr});
        createModelAndSavePredictions(pipeline, searchProfileId);
    }
}
