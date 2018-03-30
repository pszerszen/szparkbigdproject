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
import com.pgs.spark.bigdata.domain.SearchCriteria;
import com.pgs.spark.bigdata.repository.SearchCriteriaRepository;
import com.pgs.spark.bigdata.service.SearchCriteriaService;
import com.pgs.spark.bigdata.web.rest.dto.SearchCriteriaDTO;
import com.pgs.spark.bigdata.web.rest.mapper.SearchCriteriaMapper;


/**
 * Test class for the SearchCriteriaResource REST controller.
 *
 * @see SearchCriteriaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AnalyzerApp.class)
@WebAppConfiguration
@IntegrationTest
public class SearchCriteriaResourceIntTest {

    private static final String DEFAULT_KEY_WORD = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_KEY_WORD = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_MUST_HAVE_WORD = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_MUST_HAVE_WORD = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_EXCLUDED_WORD = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_EXCLUDED_WORD = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private SearchCriteriaRepository searchCriteriaRepository;

    @Inject
    private SearchCriteriaMapper searchCriteriaMapper;

    @Inject
    private SearchCriteriaService searchCriteriaService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSearchCriteriaMockMvc;

    private SearchCriteria searchCriteria;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SearchCriteriaResource searchCriteriaResource = new SearchCriteriaResource();
        ReflectionTestUtils.setField(searchCriteriaResource, "searchCriteriaService", searchCriteriaService);
        ReflectionTestUtils.setField(searchCriteriaResource, "searchCriteriaMapper", searchCriteriaMapper);
        this.restSearchCriteriaMockMvc = MockMvcBuilders.standaloneSetup(searchCriteriaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        searchCriteria = new SearchCriteria();
        searchCriteria.setKeyWord(DEFAULT_KEY_WORD);
        searchCriteria.setMustHaveWord(DEFAULT_MUST_HAVE_WORD);
        searchCriteria.setExcludedWord(DEFAULT_EXCLUDED_WORD);
    }

    @Test
    @Transactional
    public void createSearchCriteria() throws Exception {
        int databaseSizeBeforeCreate = searchCriteriaRepository.findAll().size();

        // Create the SearchCriteria
        SearchCriteriaDTO searchCriteriaDTO = searchCriteriaMapper.searchCriteriaToSearchCriteriaDTO(searchCriteria);

        restSearchCriteriaMockMvc.perform(post("/api/search-criteria")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(searchCriteriaDTO)))
                .andExpect(status().isCreated());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriteria = searchCriteriaRepository.findAll();
        assertThat(searchCriteria).hasSize(databaseSizeBeforeCreate + 1);
        SearchCriteria testSearchCriteria = searchCriteria.get(searchCriteria.size() - 1);
        assertThat(testSearchCriteria.getKeyWord()).isEqualTo(DEFAULT_KEY_WORD);
        assertThat(testSearchCriteria.getMustHaveWord()).isEqualTo(DEFAULT_MUST_HAVE_WORD);
        assertThat(testSearchCriteria.getExcludedWord()).isEqualTo(DEFAULT_EXCLUDED_WORD);
    }

    @Test
    @Transactional
    public void checkKeyWordIsRequired() throws Exception {
        int databaseSizeBeforeTest = searchCriteriaRepository.findAll().size();
        // set the field null
        searchCriteria.setKeyWord(null);

        // Create the SearchCriteria, which fails.
        SearchCriteriaDTO searchCriteriaDTO = searchCriteriaMapper.searchCriteriaToSearchCriteriaDTO(searchCriteria);

        restSearchCriteriaMockMvc.perform(post("/api/search-criteria")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(searchCriteriaDTO)))
                .andExpect(status().isBadRequest());

        List<SearchCriteria> searchCriteria = searchCriteriaRepository.findAll();
        assertThat(searchCriteria).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSearchCriteria() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);

        // Get all the searchCriteria
        restSearchCriteriaMockMvc.perform(get("/api/search-criteria?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(searchCriteria.getId().intValue())))
                .andExpect(jsonPath("$.[*].keyWord").value(hasItem(DEFAULT_KEY_WORD.toString())))
                .andExpect(jsonPath("$.[*].mustHaveWord").value(hasItem(DEFAULT_MUST_HAVE_WORD.toString())))
                .andExpect(jsonPath("$.[*].excludedWord").value(hasItem(DEFAULT_EXCLUDED_WORD.toString())));
    }

    @Test
    @Transactional
    public void getSearchCriteria() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);

        // Get the searchCriteria
        restSearchCriteriaMockMvc.perform(get("/api/search-criteria/{id}", searchCriteria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(searchCriteria.getId().intValue()))
            .andExpect(jsonPath("$.keyWord").value(DEFAULT_KEY_WORD.toString()))
            .andExpect(jsonPath("$.mustHaveWord").value(DEFAULT_MUST_HAVE_WORD.toString()))
            .andExpect(jsonPath("$.excludedWord").value(DEFAULT_EXCLUDED_WORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSearchCriteria() throws Exception {
        // Get the searchCriteria
        restSearchCriteriaMockMvc.perform(get("/api/search-criteria/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSearchCriteria() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);
        int databaseSizeBeforeUpdate = searchCriteriaRepository.findAll().size();

        // Update the searchCriteria
        SearchCriteria updatedSearchCriteria = new SearchCriteria();
        updatedSearchCriteria.setId(searchCriteria.getId());
        updatedSearchCriteria.setKeyWord(UPDATED_KEY_WORD);
        updatedSearchCriteria.setMustHaveWord(UPDATED_MUST_HAVE_WORD);
        updatedSearchCriteria.setExcludedWord(UPDATED_EXCLUDED_WORD);
        SearchCriteriaDTO searchCriteriaDTO = searchCriteriaMapper.searchCriteriaToSearchCriteriaDTO(updatedSearchCriteria);

        restSearchCriteriaMockMvc.perform(put("/api/search-criteria")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(searchCriteriaDTO)))
                .andExpect(status().isOk());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriteria = searchCriteriaRepository.findAll();
        assertThat(searchCriteria).hasSize(databaseSizeBeforeUpdate);
        SearchCriteria testSearchCriteria = searchCriteria.get(searchCriteria.size() - 1);
        assertThat(testSearchCriteria.getKeyWord()).isEqualTo(UPDATED_KEY_WORD);
        assertThat(testSearchCriteria.getMustHaveWord()).isEqualTo(UPDATED_MUST_HAVE_WORD);
        assertThat(testSearchCriteria.getExcludedWord()).isEqualTo(UPDATED_EXCLUDED_WORD);
    }

    @Test
    @Transactional
    public void deleteSearchCriteria() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);
        int databaseSizeBeforeDelete = searchCriteriaRepository.findAll().size();

        // Get the searchCriteria
        restSearchCriteriaMockMvc.perform(delete("/api/search-criteria/{id}", searchCriteria.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SearchCriteria> searchCriteria = searchCriteriaRepository.findAll();
        assertThat(searchCriteria).hasSize(databaseSizeBeforeDelete - 1);
    }
}
