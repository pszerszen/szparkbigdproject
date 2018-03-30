package com.pgs.spark.bigdata.service.impl;

import com.google.common.collect.Lists;
import com.pgs.spark.bigdata.service.DocumentService;
import com.pgs.spark.bigdata.domain.Document;
import com.pgs.spark.bigdata.repository.DocumentRepository;
import com.pgs.spark.bigdata.service.util.RandomUtil;
import com.pgs.spark.bigdata.web.rest.dto.DocumentDTO;
import com.pgs.spark.bigdata.web.rest.mapper.DocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Document.
 */
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService{

    private final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    @Inject
    private DocumentRepository documentRepository;

    @Inject
    private DocumentMapper documentMapper;

    /**
     * Save a document.
     *
     * @param documentDTO the entity to save
     * @return the persisted entity
     */
    public DocumentDTO save(DocumentDTO documentDTO) {
        log.debug("Request to save Document : {}", documentDTO);
        Document document = documentMapper.documentDTOToDocument(documentDTO);
        document = documentRepository.save(document);
        DocumentDTO result = documentMapper.documentToDocumentDTO(document);
        return result;
    }

    /**
     *  Get all the documents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Document> findAll(Pageable pageable) {
        log.debug("Request to get all Documents");
        Page<Document> result = documentRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one document by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public DocumentDTO findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        Document document = documentRepository.findOne(id);
        DocumentDTO documentDTO = documentMapper.documentToDocumentDTO(document);
        return documentDTO;
    }

    /**
     *  Delete the  document by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentRepository.delete(id);
    }

    @Override
    public void shuffleDocumentsDates(final LocalDate from, final LocalDate until) {
        log.debug("Request to shuffle all Documents dates");

        Page<Document> chunk = documentRepository.findAll(new PageRequest(0, 100));
        long parsed = 0L;
        while (chunk.hasContent()){
            final List<Document> documents = chunk.getContent();
            documents.forEach(document -> document.setUpdateDate(RandomUtil.generateLocalDateInRange(from, until)));

            documentRepository.save(documents);
            documentRepository.flush();

            parsed += chunk.getNumberOfElements();
            log.debug("Updated {} of {} dates, {} to go.", parsed, chunk.getTotalElements(), chunk.getTotalElements() - parsed);

            chunk = chunk.hasNext() ? documentRepository.findAll(chunk.nextPageable()) : new PageImpl<>(Lists.newArrayList());
        }
    }

    public List<DocumentDTO> listByIds(List<Long> documentsIds){
        return documentMapper.documentsToDocumentDTOs(documentRepository.findAll(documentsIds));
    }
}
