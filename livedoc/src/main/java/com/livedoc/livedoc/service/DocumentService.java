package com.livedoc.livedoc.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.livedoc.livedoc.exception.ErrorCode;
import com.livedoc.livedoc.exception.LiveDocException;
import com.livedoc.livedoc.model.Document;
import com.livedoc.livedoc.model.DocumentDetailResponse;
import com.livedoc.livedoc.model.DocumentListResponse;
import com.livedoc.livedoc.model.DocumentResponse;
import com.livedoc.livedoc.model.User;
import com.livedoc.livedoc.repository.DocumentRespository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {
    
    private final DocumentRespository documentRespository;

    public Document createDocument(String title, String content, User owner){
       
        boolean exists = documentRespository.existsByOwnerIdAndTitle(owner.getId(), title);
        if(exists){
            throw new LiveDocException(ErrorCode.DOCUMENT_ALREADY_EXISTS);
        }
       
        Document document = Document.builder()
                            .title(title)
                            .content(content)
                            .owner(owner)
                            .createdAt(Instant.now().toEpochMilli())
                            .updatedAt(Instant.now().toEpochMilli())
                            .build();
        return documentRespository.save(document);
    }

    public List<DocumentListResponse> getMyDocuments(UUID ownerId){
        List<Document> docs = documentRespository.findByOwnerId(ownerId);
        return docs.stream()
              .map(doc -> new DocumentListResponse(doc.getId(), doc.getTitle(), doc.getUpdatedAt()))
              .collect(Collectors.toList());
    }

    public DocumentDetailResponse getDocumentById(UUID documentId, UUID ownerId){
        Document document = documentRespository.findById(documentId)
                            .orElseThrow(() -> new RuntimeException("Document not found"));
        
        if(!document.getOwner().getId().equals(ownerId)){
            throw new RuntimeException("Unauthorized access");
        }

        return new DocumentDetailResponse(documentId, document.getTitle(), document.getContent(), document.getUpdatedAt());
    }

    public Document updateDocument(UUID documentId, String newTitle, String newContent, UUID ownerId){
        Document document = documentRespository.findById(documentId)
                            .orElseThrow(() -> new RuntimeException("Document not found"));
        
        if(!document.getOwner().getId().equals(ownerId)){
            throw new RuntimeException("Unauthorized access");
        }

        document.setTitle(newTitle);
        document.setContent(newContent);
        document.setUpdatedAt(Instant.now().toEpochMilli());

        return documentRespository.save(document);
    }

    public void deleteDocument(UUID documentId, UUID ownerId){
        Document document = documentRespository.findById(documentId)
                            .orElseThrow(() -> new RuntimeException("Document not found"));
        
        if(!document.getOwner().getId().equals(ownerId)){
            throw new RuntimeException("Unauthorized access");
        }

        documentRespository.delete(document);
    }
    
    
}
