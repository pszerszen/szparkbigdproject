package com.pgs.spark.bigdata.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pgs.spark.bigdata.domain.SearchCriteria;


/**
 * A DTO for the SearchProfile entity.
 */
public class SearchProfileDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private Set<SearchCriteria> searchCriteria = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        final SearchProfileDTO searchProfileDTO = (SearchProfileDTO) o;

        if ( ! Objects.equals(id, searchProfileDTO.id)) {
			return false;
		}

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SearchProfileDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }

	public Set<SearchCriteria> getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(Set<SearchCriteria> searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
}
