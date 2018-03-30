package com.pgs.spark.bigdata.processor.mappers;

import org.springframework.stereotype.Component;

import com.pgs.spark.bigdata.processor.domain.Result;
import com.pgs.spark.bigdata.processor.dto.Content;
import com.pgs.spark.bigdata.processor.dto.LabeledContent;

@Component
public class ResultMappers {

    public Content toContent(Result result){
        return new Content(result.getId(), result.getDocument().getContent());
    }

    public LabeledContent toLabeledContent(Result result){
        return new LabeledContent(result.getId(), result.getDocument().getContent(), result.getClassification().getSparkLabel());
    }
}
