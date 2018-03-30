package com.pgs.spark.bigdata.algorithmComparator.jobs;

import com.pgs.spark.bigdata.algorithmComparator.domain.Classification;
import com.pgs.spark.bigdata.algorithmComparator.domain.ClassificationGroup;
import com.pgs.spark.bigdata.algorithmComparator.domain.Result;
import com.pgs.spark.bigdata.algorithmComparator.dto.Content;
import com.pgs.spark.bigdata.algorithmComparator.dto.LabeledContent;
import com.pgs.spark.bigdata.algorithmComparator.mappers.ResultMappers;
import com.pgs.spark.bigdata.algorithmComparator.repository.ClassificationGroupRepository;
import com.pgs.spark.bigdata.algorithmComparator.repository.ResultRepository;
import com.pgs.spark.bigdata.algorithmComparator.service.ClassificationGroupService;
import org.apache.spark.ml.Estimator;
import org.apache.spark.ml.Model;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SparkJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(SparkJob.class);

    private static final int SQL_QUERY_LIMIT = 200;

    private static final int NUM_FEATURES_POWER = 10;

    @Autowired
    protected SQLContext sqlContext;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ClassificationGroupService classificationGroupService;

    @Autowired
    private ResultMappers resultMappers;

    public abstract void run(final Long searchProfileId);

    protected int getMaxFeatures(){
        return (int) Math.pow(2, NUM_FEATURES_POWER);
    }

    protected void createModelAndSavePredictions(Estimator<?> estimator, final Long searchProfileId) {
        DataFrame dataFrame = getTrainingData(searchProfileId);
        final Model<?> model = (Model<?>) estimator.fit(dataFrame);

        Page<Result> chunk = resultRepository.findBySearchProfileToEstimating(searchProfileId, new PageRequest(0, SQL_QUERY_LIMIT));

        boolean fetchNextPage = true;
        while (fetchNextPage) {
            DataFrame testData = sqlContext.createDataFrame(chunk.getContent()
                    .stream()
                    .map(resultMappers::toContent)
                    .collect(Collectors.toList()), Content.class);

            DataFrame predictions = model.transform(testData);

            predictions.select("id", "prediction").collectAsList()
                    .stream()
                    .forEach(row -> {
                        Result result = resultRepository.findOne(row.getLong(0));
                        Classification classification = Classification.fromLabelLinear(row.getDouble(1));
                        classificationGroupService.saveClassification(this, result, classification);
                        LOGGER.debug("The result with id: {} has been automatically classified as {} article by {}.",
                                result.getId(),
                                classification.name(),
                                this.getClass().getSimpleName());
                            });

            chunk = resultRepository.findBySearchProfileToEstimating(searchProfileId, chunk.nextPageable());
            fetchNextPage = chunk.hasNext();
        }
    }

    private DataFrame getTrainingData(Long searchProfileId) {
        List<Result> resultList = resultRepository.findBySearchProfileToTraining(searchProfileId);
        return sqlContext.createDataFrame(resultList
                .stream()
                .map(resultMappers::toLabeledContent)
                .collect(Collectors.toList()), LabeledContent.class);
    }
}
