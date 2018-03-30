package com.pgs.spark.bigdata.algorithmComparator.mappers;

import com.pgs.spark.bigdata.algorithmComparator.domain.Result;
import com.pgs.spark.bigdata.algorithmComparator.dto.Content;
import com.pgs.spark.bigdata.algorithmComparator.dto.LabeledContent;
import org.springframework.stereotype.Component;

@Component
public class ResultMappers {

    public Content toContent(Result result){
        return new Content(result.getId(), result.getDocument().getContent());
    }

    public LabeledContent toLabeledContent(Result result){
        return new LabeledContent(result.getId(), result.getDocument().getContent(), result.getClassification().getSparkLabel());
    }
}
