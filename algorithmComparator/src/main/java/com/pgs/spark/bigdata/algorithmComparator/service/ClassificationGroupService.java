package com.pgs.spark.bigdata.algorithmComparator.service;

import com.pgs.spark.bigdata.algorithmComparator.domain.Classification;
import com.pgs.spark.bigdata.algorithmComparator.domain.Result;
import com.pgs.spark.bigdata.algorithmComparator.dto.ChartDataDTO;
import com.pgs.spark.bigdata.algorithmComparator.dto.ChartScale;
import com.pgs.spark.bigdata.algorithmComparator.jobs.SparkJob;

import java.util.Date;
import java.util.Map;

public interface ClassificationGroupService {
    void saveClassification(SparkJob job, Result result, Classification classification);

    void performClassificationUsingAllAlgorithms(Long searchProfileId);

    Map<String, ChartDataDTO> getChartData(Long searchProfile, ChartScale scale, Date from, Date until);
}
