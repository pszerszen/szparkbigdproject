package com.pgs.spark.bigdata.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.drew.lang.annotations.NotNull;

@Entity
@Table(name = "document")
public class Document extends AbstractEntity {

    private static final long serialVersionUID = -6533133288981763506L;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "creationDate")
    private Date creationDate;

    @Column(name = "updateDate")
    private Date updateDate;

    @Column(name = "title")
    private String title;

    @ElementCollection
    @CollectionTable(name = "Tags", joinColumns = @JoinColumn(name = "document_id"))
    private Set<String> tags;

    @OneToMany(mappedBy = "document")
    private Set<Result> results = new HashSet<>();

    public Document() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(final Date updateDate) {
        this.updateDate = updateDate;
    }

    public Set<Result> getResults() {
        return results;
    }

    public void setResults(final Set<Result> results) {
        this.results = results;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
    
    @Override
    public boolean equals(Object obj) {
    	return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    @Override
    public int hashCode() {
    	return HashCodeBuilder.reflectionHashCode(this);
    }
}
