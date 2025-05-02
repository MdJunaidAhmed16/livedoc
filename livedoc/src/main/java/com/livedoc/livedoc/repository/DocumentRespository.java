package com.livedoc.livedoc.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.livedoc.livedoc.model.Document;

public interface DocumentRespository extends JpaRepository<Document, UUID> {
    List<Document> findByOwnerId(UUID id);
}
