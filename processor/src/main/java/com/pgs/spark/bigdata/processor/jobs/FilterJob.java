package com.pgs.spark.bigdata.processor.jobs;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pgs.spark.bigdata.processor.config.AppConfig;
import com.pgs.spark.bigdata.processor.config.DbConfig;
import com.pgs.spark.bigdata.processor.domain.Document;
import com.pgs.spark.bigdata.processor.domain.Result;
import com.pgs.spark.bigdata.processor.domain.SearchCriteria;
import com.pgs.spark.bigdata.processor.domain.SearchProfile;
import com.pgs.spark.bigdata.processor.repository.ResultRepository;
import com.pgs.spark.bigdata.processor.repository.SearchProfileRepository;

@Component
public class FilterJob extends SparkJob {

    @Autowired
    private DbConfig dbConfig;
    @Autowired
    private SearchProfileRepository searchProfileRepository;
    @Autowired
    private ResultRepository resultRepository;

    public void run(final Long searchProfileId) {
        SearchProfile searchProfile = searchProfileRepository.findOne(searchProfileId);
        Column filterCondition = createFilterCondition(searchProfile);
        DataFrame df = sqlContext.read().jdbc(dbConfig.getUrl(), "document", dbConfig.getJdbcProps());
        df.registerTempTable("document");
        List<Row> results = df.sqlContext()
                .sql("select id, content from document")
                .filter(filterCondition)
                .select("id")
                .collectAsList();
        insertResultsIfNotExist(results, searchProfile);
    }

    private Column createFilterCondition(SearchProfile searchProfile) {
        Column content = new Column("content");
        Column aggregateCondition = null;
        for (SearchCriteria searchCriteria : searchProfile.getSearchCriteria()) {
            Column condition = containsWholeWord(content, searchCriteria.getKeyWord());
            if (StringUtils.isNotEmpty(searchCriteria.getMustHaveWord())) {
                condition = condition.and(containsWholeWord(content, searchCriteria.getMustHaveWord()));
            }
            if (StringUtils.isNotEmpty(searchCriteria.getExcludedWord())) {
                condition = condition.and(functions.not(containsWholeWord(content, searchCriteria.getExcludedWord())));
            }
            aggregateCondition = (aggregateCondition == null) ? condition : aggregateCondition.and(condition);
        }
        return aggregateCondition;
    }

    private Column containsWholeWord(Column content, String word) {
        //return content.rlike("[[:<:]]" + word + "[[:>:]]"); // does not work :(
        //return content.like("%[^a-zA-Z0-9]" + word + "[^a-zA-Z0-9]%"); // does not work either :(
        return content.contains(word); // workaround
    }

    private void insertResultsIfNotExist(List<Row> rows, SearchProfile searchProfile) {
        for (Row row : rows) {
            Long documentId = row.getLong(0);
            List<Result> results = resultRepository.findByDocumentIdAndSearchProfileId(documentId, searchProfile.getId());
            if (results.isEmpty()) {
                Document document = new Document();
                document.setId(documentId);
                Result result = new Result();
                result.setDocument(document);
                result.setSearchProfile(searchProfile);
                resultRepository.save(result);
            }
        }
    }

}
