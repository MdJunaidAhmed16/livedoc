package com.livedoc.livedoc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DocumentResponse {
    private UUID id;
    private String title;
    private String content;
    private UUID ownerId;
    private String ownerName;
    private Long updatedAt;
}
