package com.pgs.spark.bigdata.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgs.spark.bigdata.entities.Document;
import com.pgs.spark.bigdata.repository.DocumentRepository;
import com.pgs.spark.bigdata.to.ArticleTO;

import static java.util.Objects.isNull;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repository;

    public void addDocument(String url, ArticleTO articleContent) {
    	if(StringUtils.isEmpty(articleContent.getContent())) {
    		return;
    	}
        final Document doc = Optional.ofNullable(repository.findByUrl(url)).orElse(new Document());
    	doc.setContent(articleContent.getContent());
        if(isNull(doc.getCreationDate())) {
            doc.setCreationDate(articleContent.getDate());
        }
        doc.setUpdateDate(articleContent.getDate());
        doc.setUrl(url);
        doc.setTitle(articleContent.getTitle());
        doc.setTags(articleContent.getTags());
        repository.save(doc);
    }

    public Document addDocument(String url, String content, Date creationDate, Date updateDate) {
        final Document doc = Optional.ofNullable(repository.findByUrl(url)).orElse(new Document());
        doc.setContent(content);
        doc.setUrl(url);
        if(isNull(doc.getCreationDate())) {
            doc.setCreationDate(creationDate);
        }
        doc.setUpdateDate(updateDate);
        return repository.save(doc);
    }
}
