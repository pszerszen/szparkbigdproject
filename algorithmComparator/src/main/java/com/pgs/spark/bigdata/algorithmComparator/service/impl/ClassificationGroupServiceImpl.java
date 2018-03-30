package com.pgs.spark.bigdata.algorithmComparator.service.impl;

import com.pgs.spark.bigdata.algorithmComparator.domain.Classification;
import com.pgs.spark.bigdata.algorithmComparator.domain.ClassificationGroup;
import com.pgs.spark.bigdata.algorithmComparator.domain.Result;
import com.pgs.spark.bigdata.algorithmComparator.dto.ChartDataDTO;
import com.pgs.spark.bigdata.algorithmComparator.dto.ChartScale;
import com.pgs.spark.bigdata.algorithmComparator.jobs.Algorithm;
import com.pgs.spark.bigdata.algorithmComparator.jobs.CrossValidationEstimationJob;
import com.pgs.spark.bigdata.algorithmComparator.jobs.MultilayerPerceptronClassifierJob;
import com.pgs.spark.bigdata.algorithmComparator.jobs.SimpleLogisticRegressionJob;
import com.pgs.spark.bigdata.algorithmComparator.jobs.SparkJob;
import com.pgs.spark.bigdata.algorithmComparator.jobs.TrainValidationEstimationJob;
import com.pgs.spark.bigdata.algorithmComparator.repository.ClassificationGroupRepository;
import com.pgs.spark.bigdata.algorithmComparator.service.ClassificationGroupService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ClassificationGroupServiceImpl implements ClassificationGroupService {

    private static final String DATE_SEPARATOR = "/";

    @Autowired
    private ClassificationGroupRepository classificationGroupRepository;

    @Resource
    private List<SparkJob> algorithms;

    @Override
    public void saveClassification(SparkJob job, Result result, Classification classification) {
        ClassificationGroup classificationGroup = classificationGroupRepository
                .findByResultId(result.getId()).orElseGet(() -> {
                            ClassificationGroup newClassificationGroup = new ClassificationGroup();
                            newClassificationGroup.setResult(result);
                            return newClassificationGroup;
                        }
                );
        if(job instanceof CrossValidationEstimationJob){
            classificationGroup.setCrossValidatorClassification(classification);
        }
        else if(job instanceof MultilayerPerceptronClassifierJob){
            classificationGroup.setMultilayerPerceptronClassification(classification);
        }
        else if(job instanceof SimpleLogisticRegressionJob){
            classificationGroup.setSimpleClassification(classification);
        }
        else if(job instanceof TrainValidationEstimationJob){
            classificationGroup.setTrainValidatorClassification(classification);
        }
        classificationGroupRepository.save(classificationGroup);
    }

    @Override
    public void performClassificationUsingAllAlgorithms(Long searchProfileId) {
        algorithms.forEach(algorithm -> algorithm.run(searchProfileId));
    }

    @Override
    public Map<String, ChartDataDTO> getChartData(Long searchProfile, ChartScale scale, Date from, Date until) {
        Map<String, ChartDataDTO> algorithmData = new HashMap<>();
        for(Algorithm algorithm: Algorithm.values()){
            Pair<String, ChartDataDTO> data = prepareDataForAlgorithm(searchProfile, scale, from, until, algorithm);
            algorithmData.put(data.getLeft(), data.getRight());
        }
        return algorithmData;
    }

    private String prepareLabel(ChartScale scale, Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return DateTimeFormatter
                .ofPattern(scale.getPattern())
                .format(localDate);
    }

    private Pair<String, ChartDataDTO> prepareDataForAlgorithm(final Long searchProfile, final ChartScale scale, final Date from, final Date until, final Algorithm algorithm){
        ChartDataDTO dto = new ChartDataDTO();

        Map<Pair<Classification, String>, Long> numbers = new TreeMap<>();
        for (Classification classification : Classification.values()) {
            List<Object[]> classificationGroups = Collections.emptyList();
            switch (algorithm){
                case SIMPLE:
                    classificationGroups = classificationGroupRepository.findResultsBySearchProfileAndSimpleClassificationInRange(searchProfile, classification, from, until);
                    break;
                case CROSS_VALIDATION:
                    classificationGroups = classificationGroupRepository.findResultsBySearchProfileAndCrossClassificationInRange(searchProfile, classification, from, until);
                    break;
                case TRAIN_VALIDATION:
                    classificationGroups = classificationGroupRepository.findResultsBySearchProfileAndTrainClassificationInRange(searchProfile, classification, from, until);
                    break;
                case MULTILAYER_PERCEPTRON:
                    classificationGroups = classificationGroupRepository.findResultsBySearchProfileAndMultilayerClassificationInRange(searchProfile, classification, from, until);
                    break;
            }
            classificationGroups
                    .stream()
                    .map((objects) -> Pair.of((Date) objects[0], (Long) objects[1]))
                    .forEach(localDateLongPair -> numbers.merge(
                            Pair.of(classification, prepareLabel(scale, localDateLongPair.getLeft())),
                            localDateLongPair.getRight(),
                            (a, b) -> a + b));
        }

        dto.setLabels(numbers.keySet()
                .stream()
                .map(Pair::getRight)
                .collect(Collectors.toSet()).stream()
                .sorted(this::stringifiedDateComparator)
                .collect(Collectors.toList()));
        final List<String> labels = dto.getLabels();

        for (Classification classification : dto.getSeries()) {
            Long[] dataSet = initialDataSet(labels.size());
            numbers.keySet().stream()
                    .filter(key -> classification.equals(key.getLeft()))
                    .forEach(pair -> dataSet[ labels.indexOf(pair.getRight()) ] += numbers.get(pair));
            dto.addDataSet(dataSet);
        }

        return Pair.of(algorithm.name(), dto);
    }

    private Long[] initialDataSet(int size) {
        Long[] dataSet = new Long[ size ];
        for (int i = 0; i < size; i++) {
            dataSet[ i ] = 0L;
        }
        return dataSet;
    }

    private int stringifiedDateComparator(String a, String b) {
        String[] aDate = a.split(DATE_SEPARATOR);
        String[] bDate = b.split(DATE_SEPARATOR);

        for (int i = aDate.length - 1; i >= 0; i--) {
            Integer aNum = Integer.parseInt(aDate[ i ]);
            Integer bNum = Integer.parseInt(bDate[ i ]);

            int compare = aNum.compareTo(bNum);
            if (compare != 0) {
                return compare;
            }
        }

        return 0;
    }
}
