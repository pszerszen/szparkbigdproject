package com.pgs.spark.bigdata.service;

import static org.mockito.Mockito.verify;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.pgs.spark.bigdata.entities.Document;
import com.pgs.spark.bigdata.repository.DocumentRepository;
import com.pgs.spark.bigdata.to.ArticleTO;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {

	@Mock
	private DocumentRepository repository;

	@InjectMocks
	private DocumentService documentService;

	@Test
	public void testAddDocumentStringArticleTO() throws Exception {
		// given
		final String url = "www.wp.pl";
		final String content = RandomStringUtils.random(100);
		final Set<String> tags = Stream.generate(() -> RandomStringUtils.random(5)).limit(5).collect(Collectors.toSet());
		final String title = RandomStringUtils.random(10);
		final Date date = new Date();

		final ArticleTO articleContent = new ArticleTO();
		articleContent.setContent(content);
		articleContent.setTags(tags);
		articleContent.setTitle(title);
		articleContent.setDate(date);

		// when
		documentService.addDocument(url, articleContent);

		// then
		final Document document = new Document();
		document.setContent(content);
		document.setTags(tags);
		document.setTitle(title);
		document.setUrl(url);
		document.setCreationDate(date);
		document.setUpdateDate(date);

		verify(repository).save(document);
	}

	@Test
	public void testAddDocumentStringStringDateDate() throws Exception {
		// given
		final String url = "www.wp.pl";
		final String content = RandomStringUtils.random(10);
		final Date updateDate = new Date();
		final Date creationDate = SimpleDateFormat.getDateInstance().parse("10-10-2014");
		// when
		documentService.addDocument(url, content, creationDate, updateDate);

		// then
		final Document document = new Document();
		document.setContent(content);
		document.setUrl(url);
		document.setCreationDate(creationDate);
		document.setUpdateDate(updateDate);

		verify(repository).save(document);
	}

}
