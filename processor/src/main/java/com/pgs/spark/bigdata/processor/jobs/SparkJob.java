package com.pgs.spark.bigdata.processor.jobs;

import com.pgs.spark.bigdata.processor.domain.Classification;
import com.pgs.spark.bigdata.processor.domain.Result;
import com.pgs.spark.bigdata.processor.dto.Content;
import com.pgs.spark.bigdata.processor.dto.LabeledContent;
import com.pgs.spark.bigdata.processor.mappers.ResultMappers;
import com.pgs.spark.bigdata.processor.repository.ResultRepository;
import org.apache.spark.ml.Estimator;
import org.apache.spark.ml.Model;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

@PropertySource("classpath:application.properties")
public abstract class SparkJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(SparkJob.class);

    private static final int SQL_QUERY_LIMIT = 200;

    private static final int NUM_FEATURES_POWER = 10;

    @Value("#{'${algorithms.numFeaturesTable}'.split(',')}")
    protected int[] numFeaturesTable;

    @Value("#{'${algorithms.regParamTable}'.split(',')}")
    protected double[] regParamTable;

    @Value("#{'${algorithms.elasticNetParamTable}'.split(',')}")
    protected double[] elasticNetParamTable;

    @Value("#{'${algorithms.maxIterTable}'.split(',')}")
    protected int[] maxIterTable;

    @Autowired
    protected SQLContext sqlContext;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ResultMappers resultMappers;

    protected int getMaxFeatures(){
        return (int) Math.pow(2, NUM_FEATURES_POWER);
    }

    protected Tokenizer getTokenizer(){
        return new Tokenizer()
                .setInputCol("text")
                .setOutputCol("words");
    }

    protected HashingTF getHashingTF(Tokenizer tokenizer){
        return new HashingTF()
                .setNumFeatures(getMaxFeatures())
                .setInputCol(tokenizer.getOutputCol())
                .setOutputCol("features");
    }

    public abstract void run(final Long searchProfileId);

    protected void createModelAndSavePredictions(Estimator<?> estimator, final Long searchProfileId) {

        DataFrame dataFrame = getTrainingData(searchProfileId);
        final Model<?> model = (Model<?>) estimator.fit(dataFrame);

        Page<Result> chunk = resultRepository.findBySearchProfileToEstimating(searchProfileId, new PageRequest(0, SQL_QUERY_LIMIT));

        while (chunk.hasContent()) {
            DataFrame testData = sqlContext.createDataFrame(chunk.getContent()
                    .stream()
                    .map(resultMappers::toContent)
                    .collect(Collectors.toList()), Content.class);

            DataFrame predictions = model.transform(testData);

            resultRepository.save(predictions.select("id", "prediction").collectAsList()
                    .stream()
                    .map(row -> {
                        Result result = resultRepository.findOne(row.getLong(0));
                        result.setClassification(Classification.fromLabelLinear(row.getDouble(1)));
                        return result;
                    })
                    .collect(Collectors.toList()));

            chunk = resultRepository.findBySearchProfileToEstimating(searchProfileId, chunk.nextPageable());
        }
    }

    private DataFrame getTrainingData(Long searchProfileId) {
        return sqlContext.createDataFrame(resultRepository.findBySearchProfileToTraining(searchProfileId)
                .stream()
                .map(resultMappers::toLabeledContent)
                .collect(Collectors.toList()), LabeledContent.class);
    }

}
