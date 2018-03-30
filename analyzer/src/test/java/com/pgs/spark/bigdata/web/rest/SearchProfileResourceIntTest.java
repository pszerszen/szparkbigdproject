package com.pgs.spark.bigdata.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.pgs.spark.bigdata.AnalyzerApp;
import com.pgs.spark.bigdata.domain.SearchProfile;
import com.pgs.spark.bigdata.repository.SearchProfileRepository;
import com.pgs.spark.bigdata.service.SearchProfileService;
import com.pgs.spark.bigdata.web.rest.dto.SearchProfileDTO;
import com.pgs.spark.bigdata.web.rest.mapper.SearchProfileMapper;


/**
 * Test class for the SearchProfileResource REST controller.
 *
 * @see SearchProfileResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AnalyzerApp.class)
@WebAppConfiguration
@IntegrationTest
public class SearchProfileResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private SearchProfileRepository searchProfileRepository;

    @Inject
    private SearchProfileMapper searchProfileMapper;

    @Inject
    private SearchProfileService searchProfileService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSearchProfileMockMvc;

    private SearchProfile searchProfile;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SearchProfileResource searchProfileResource = new SearchProfileResource();
        ReflectionTestUtils.setField(searchProfileResource, "searchProfileService", searchProfileService);
        ReflectionTestUtils.setField(searchProfileResource, "searchProfileMapper", searchProfileMapper);
        this.restSearchProfileMockMvc = MockMvcBuilders.standaloneSetup(searchProfileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        searchProfile = new SearchProfile();
        searchProfile.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSearchProfile() throws Exception {
        final int databaseSizeBeforeCreate = searchProfileRepository.findAll().size();

        // Create the SearchProfile
        final SearchProfileDTO searchProfileDTO = searchProfileMapper.searchProfileToSearchProfileDTO(searchProfile);

        restSearchProfileMockMvc.perform(post("/api/search-profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(searchProfileDTO)))
                .andExpect(status().isCreated());

        // Validate the SearchProfile in the database
        final List<SearchProfile> searchProfiles = searchProfileRepository.findAll();
        assertThat(searchProfiles).hasSize(databaseSizeBeforeCreate + 1);
        final SearchProfile testSearchProfile = searchProfiles.get(searchProfiles.size() - 1);
        assertThat(testSearchProfile.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        final int databaseSizeBeforeTest = searchProfileRepository.findAll().size();
        // set the field null
        searchProfile.setName(null);

        // Create the SearchProfile, which fails.
        final SearchProfileDTO searchProfileDTO = searchProfileMapper.searchProfileToSearchProfileDTO(searchProfile);

        restSearchProfileMockMvc.perform(post("/api/search-profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(searchProfileDTO)))
                .andExpect(status().isBadRequest());

        final List<SearchProfile> searchProfiles = searchProfileRepository.findAll();
        assertThat(searchProfiles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSearchProfiles() throws Exception {
        // Initialize the database
        searchProfileRepository.saveAndFlush(searchProfile);

        // Get all the searchProfiles
        restSearchProfileMockMvc.perform(get("/api/search-profiles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(searchProfile.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSearchProfile() throws Exception {
        // Initialize the database
        searchProfileRepository.saveAndFlush(searchProfile);

        // Get the searchProfile
        restSearchProfileMockMvc.perform(get("/api/search-profiles/{id}", searchProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(searchProfile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSearchProfile() throws Exception {
        // Get the searchProfile
        restSearchProfileMockMvc.perform(get("/api/search-profiles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSearchProfile() throws Exception {
        // Initialize the database
        searchProfileRepository.saveAndFlush(searchProfile);
        final int databaseSizeBeforeUpdate = searchProfileRepository.findAll().size();

        // Update the searchProfile
        final SearchProfile updatedSearchProfile = new SearchProfile();
        updatedSearchProfile.setId(searchProfile.getId());
        updatedSearchProfile.setName(UPDATED_NAME);
        final SearchProfileDTO searchProfileDTO = searchProfileMapper.searchProfileToSearchProfileDTO(updatedSearchProfile);

        restSearchProfileMockMvc.perform(put("/api/search-profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(searchProfileDTO)))
                .andExpect(status().isOk());

        // Validate the SearchProfile in the database
        final List<SearchProfile> searchProfiles = searchProfileRepository.findAll();
        assertThat(searchProfiles).hasSize(databaseSizeBeforeUpdate);
        final SearchProfile testSearchProfile = searchProfiles.get(searchProfiles.size() - 1);
        assertThat(testSearchProfile.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteSearchProfile() throws Exception {
        // Initialize the database
        searchProfileRepository.saveAndFlush(searchProfile);
        final int databaseSizeBeforeDelete = searchProfileRepository.findAll().size();

        // Get the searchProfile
        restSearchProfileMockMvc.perform(delete("/api/search-profiles/{id}", searchProfile.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        final List<SearchProfile> searchProfiles = searchProfileRepository.findAll();
        assertThat(searchProfiles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
