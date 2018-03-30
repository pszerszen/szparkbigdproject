package com.pgs.spark.bigdata.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "searchprofile")
public class SearchCriteria extends AbstractEntity {

    private static final long serialVersionUID = 5317757065071532799L;

    @Column(name = "keyWord", nullable = false, columnDefinition = "VARCHAR(45)")
    private String keyWord;

    @Column(name = "mustHaveWord", columnDefinition = "VARCHAR(255)")
    private String mustHaveWord;

    @Column(name = "musntHaveWord", columnDefinition = "VARCHAR(255)")
    private String musntHaveWord;

    @ManyToOne(optional = false)
    @JoinColumn(name = "searchProfile_id")
    private SearchProfile searchProfile;

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(final String keyWord) {
        this.keyWord = keyWord;
    }

    public String getMustHaveWord() {
        return mustHaveWord;
    }

    public void setMustHaveWord(final String mustHaveWord) {
        this.mustHaveWord = mustHaveWord;
    }

    public String getMusntHaveWord() {
        return musntHaveWord;
    }

    public void setMusntHaveWord(final String musntHaveWord) {
        this.musntHaveWord = musntHaveWord;
    }

    public SearchProfile getSearchProfile() {
        return searchProfile;
    }

    public void setSearchProfile(final SearchProfile searchProfile) {
        this.searchProfile = searchProfile;
    }
}
