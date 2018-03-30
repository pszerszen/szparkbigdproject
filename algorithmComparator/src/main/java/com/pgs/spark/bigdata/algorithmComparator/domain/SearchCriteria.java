package com.pgs.spark.bigdata.algorithmComparator.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "search_criteria")
public class SearchCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "key_word", length = 255, nullable = false)
    private String keyWord;

    @Size(max = 255)
    @Column(name = "must_have_word", length = 255)
    private String mustHaveWord;

    @Size(max = 255)
    @Column(name = "excluded_word", length = 255)
    private String excludedWord;

    @ManyToOne
    private SearchProfile searchProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getMustHaveWord() {
        return mustHaveWord;
    }

    public void setMustHaveWord(String mustHaveWord) {
        this.mustHaveWord = mustHaveWord;
    }

    public String getExcludedWord() {
        return excludedWord;
    }

    public void setExcludedWord(String excludedWord) {
        this.excludedWord = excludedWord;
    }

    public SearchProfile getSearchProfile() {
        return searchProfile;
    }

    public void setSearchProfile(SearchProfile searchProfile) {
        this.searchProfile = searchProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchCriteria searchCriteria = (SearchCriteria) o;
        if(searchCriteria.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, searchCriteria.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "id=" + id +
                ", keyWord='" + keyWord + "'" +
                ", mustHaveWord='" + mustHaveWord + "'" +
                ", excludedWord='" + excludedWord + "'" +
                '}';
    }
}
