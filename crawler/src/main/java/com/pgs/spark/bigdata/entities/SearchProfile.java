package com.pgs.spark.bigdata.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "searchprofile")
public class SearchProfile extends AbstractEntity {

    private static final long serialVersionUID = -3304005723658919416L;

    private static final String SEARCH_PROFILE_COLUMN_NAME = "searchProfile";

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = SEARCH_PROFILE_COLUMN_NAME)
    private Set<SearchCriteria> searchCriterias = new HashSet<>();

    @OneToMany(mappedBy = SEARCH_PROFILE_COLUMN_NAME)
    private Set<Result> results = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<SearchCriteria> getSearchCriterias() {
        return searchCriterias;
    }

    public void setSearchCriterias(final Set<SearchCriteria> searchCriterias) {
        this.searchCriterias = searchCriterias;
    }

    public Set<Result> getResults() {
        return results;
    }

    public void setResults(final Set<Result> results) {
        this.results = results;
    }
}
