package com.livedoc.livedoc.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.livedoc.livedoc.model.Document;
import com.livedoc.livedoc.model.DocumentDetailResponse;
import com.livedoc.livedoc.model.DocumentListResponse;
import com.livedoc.livedoc.model.DocumentResponse;
import com.livedoc.livedoc.model.User;
import com.livedoc.livedoc.repository.UserRepository;
import com.livedoc.livedoc.service.DocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    

    private final DocumentService documentService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<DocumentListResponse>> getMyDocuments(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(documentService.getMyDocuments(user.getId()));
    }

    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(@RequestBody Map<String, String> request, @AuthenticationPrincipal User user){

        // validate user
        if (user == null) throw new RuntimeException("Unauthenticated access");
        String title = request.get("title");
        String content = request.get("content");

        Document doc = documentService.createDocument(title, content, user);
        DocumentResponse response = new DocumentResponse(
        doc.getId(),
        doc.getTitle(),
        doc.getContent(),
        user.getId(),
        user.getUsername(),
        doc.getUpdatedAt()
    );

    return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDetailResponse> getDocumet(@PathVariable UUID id, @AuthenticationPrincipal User user){
        return ResponseEntity.ok(documentService.getDocumentById(id, user.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponse> updateDocument(@PathVariable UUID id, @RequestBody Map<String, String> request, @AuthenticationPrincipal User user){

        String title = request.get("title");
        String content = request.get("content");

        Document doc = documentService.updateDocument(id, title, content, user.getId());
        
        DocumentResponse response = new DocumentResponse(
        doc.getId(),
        doc.getTitle(),
        doc.getContent(),
        user.getId(),
        user.getUsername(),
        doc.getUpdatedAt()
    );

    return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable UUID id, @AuthenticationPrincipal User user){
        documentService.deleteDocument(id, user.getId());

        return ResponseEntity.ok("Document deleted successfully");
    }
}
