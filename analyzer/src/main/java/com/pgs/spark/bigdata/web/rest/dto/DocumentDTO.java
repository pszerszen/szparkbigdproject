package com.pgs.spark.bigdata.web.rest.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;


/**
 * A DTO for the Document entity.
 */
public class DocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private String url;


    @Lob
    private String content;


    private LocalDate creationDate;


    private LocalDate updateDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocumentDTO documentDTO = (DocumentDTO) o;

        if ( ! Objects.equals(id, documentDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DocumentDTO{" +
            "id=" + id +
            ", url='" + url + "'" +
            ", content='" + content + "'" +
            ", creationDate='" + creationDate + "'" +
            ", updateDate='" + updateDate + "'" +
            '}';
    }
}
