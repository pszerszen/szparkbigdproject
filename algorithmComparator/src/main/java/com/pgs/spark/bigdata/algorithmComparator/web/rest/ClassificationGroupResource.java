package com.pgs.spark.bigdata.algorithmComparator.web.rest;

import com.pgs.spark.bigdata.algorithmComparator.dto.ChartDataDTO;
import com.pgs.spark.bigdata.algorithmComparator.dto.ChartScale;
import com.pgs.spark.bigdata.algorithmComparator.service.ClassificationGroupService;
import com.pgs.spark.bigdata.algorithmComparator.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/algorithmComparator")
public class ClassificationGroupResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassificationGroupResource.class);

    @Autowired
    private ClassificationGroupService classificationGroupService;

    @RequestMapping(value = "/performComparison",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> performComparison(@RequestParam(value = "searchProfile", required = true) Long searchProfile) {
        classificationGroupService.performClassificationUsingAllAlgorithms(searchProfile);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Algorithm compare: ", "All")).build();
    }

    @RequestMapping(value = "/getComparisonData",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, ChartDataDTO>> getComparisonData(@RequestParam(value = "searchProfile", required = true) Long searchProfile,
                                                                       @RequestParam(value = "scale", required = true)  ChartScale scale,
                                                                       @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                                                       @RequestParam("until") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date until) {
        Map<String, ChartDataDTO> chartDataDTO = classificationGroupService.getChartData(searchProfile, scale, from, until);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Algorithm compare: ", "All")).body(chartDataDTO);
    }
}
