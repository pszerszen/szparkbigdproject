package com.pgs.spark.bigdata.processor.web.rest;

import com.google.common.base.Stopwatch;
import com.pgs.spark.bigdata.processor.jobs.FilterJob;
import com.pgs.spark.bigdata.processor.jobs.SparkJob;
import com.pgs.spark.bigdata.processor.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/processor")
@PropertySource("classpath:application.properties")
public class ProcessorResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorResource.class);

    @Autowired
    private Map<String, SparkJob> jobProvider;

    @Value("#{${jobsDescriptions}}")
    private Map<String, String> jobsDescriptions;

    @RequestMapping(value = "/possibleJobs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getPossibleJobs(){

        return jobProvider
                .entrySet()
                .stream()
                .sorted((a, b)->Boolean.valueOf(b.getValue() instanceof FilterJob).compareTo(a.getValue() instanceof FilterJob))
                .collect(Collectors.toMap(Map.Entry::getKey, entry1 -> jobsDescriptions.get(entry1.getKey()), (e1, e2) -> e1, LinkedHashMap::new));
    }

    @RequestMapping(value = "/performJob",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> performJob(@RequestParam("jobName") String jobName,
                                           @RequestParam(value = "searchProfile", required = false) Long searchProfile) {

        final Stopwatch stopwatch = Stopwatch.createStarted();
        jobProvider.get(jobName).run(searchProfile);
        stopwatch.stop();
        LOGGER.info("The job took {} seconds.", stopwatch.elapsed(TimeUnit.SECONDS));
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("process", jobName)).build();
    }
}
