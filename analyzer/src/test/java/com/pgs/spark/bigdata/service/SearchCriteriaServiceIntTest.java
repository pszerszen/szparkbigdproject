package com.pgs.spark.bigdata.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.pgs.spark.bigdata.AnalyzerApp;
import com.pgs.spark.bigdata.domain.SearchProfile;
import com.pgs.spark.bigdata.domain.enumeration.Classification;
import com.pgs.spark.bigdata.repository.SearchProfileRepository;
import com.pgs.spark.bigdata.web.rest.dto.DocumentDTO;
import com.pgs.spark.bigdata.web.rest.dto.ResultDTO;
import com.pgs.spark.bigdata.web.rest.dto.SearchCriteriaDTO;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AnalyzerApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class SearchCriteriaServiceIntTest {

    @Inject
    private SearchProfileRepository searchProfileRepository;

    @Inject
	private SearchCriteriaService searchCriteriaService;

    @Inject
	private DocumentService documentService;
    
    @Inject
    private ResultService resultService;
    
    @Test
    public void testUpdateSearchCriteria(){
    	SearchProfile profile = new SearchProfile();
    	profile.setName("testProfile");
    	profile = searchProfileRepository.save(profile);
    	final SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
    	searchCriteria.setKeyWord("test");
    	searchCriteria.setSearchProfileId(profile.getId());
    	searchCriteriaService.save(searchCriteria);
    	
    	final DocumentDTO document = new DocumentDTO();
    	document.setContent("test content");
    	document.setUrl("test url");
    	final DocumentDTO save = documentService.save(document);
    	
    	final ResultDTO resultDTO = new ResultDTO();
    	resultDTO.setClassification(Classification.POSITIVE);
    	resultDTO.setDocumentId(save.getId());
    	resultDTO.setSearchProfileId(profile.getId());
    	resultService.save(resultDTO);

    	assertThat(resultService.findAll(null)).isNotEmpty();
    	
    	searchCriteria.setMustHaveWord("must have test word");
    	searchCriteriaService.save(searchCriteria);
    	
    	assertThat(resultService.findAll(null)).isEmpty();
    	
    	SearchProfile profile2 = new SearchProfile();
    	profile2.setName("testProfile2");
    	profile2 = searchProfileRepository.save(profile2);
    	final SearchCriteriaDTO searchCriteria2 = new SearchCriteriaDTO();
    	searchCriteria2.setKeyWord("test2");
    	searchCriteria2.setSearchProfileId(profile2.getId());
    	searchCriteriaService.save(searchCriteria2);
    	
    	
    	final DocumentDTO documentDTO2 = new DocumentDTO();
    	documentDTO2.setContent("test 2");
    	documentDTO2.setUrl("test url2");
    	
    	final ResultDTO resultDTO2 = new ResultDTO();
    	resultDTO2.setClassification(Classification.POSITIVE);
    	resultDTO2.setDocumentId(save.getId());
    	resultDTO2.setSearchProfileId(profile2.getId());
    	resultService.save(resultDTO2);

    	assertThat(resultService.findAll(null)).isNotEmpty();

    	searchCriteria.setMustHaveWord("must have test word test");
    	searchCriteriaService.save(searchCriteria);

    	assertThat(resultService.findAll(null)).isNotEmpty();
    }
}

