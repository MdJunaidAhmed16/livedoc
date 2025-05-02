package com.livedoc.livedoc.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentListResponse {
    
    private UUID id;
    private String title;
    private Long updateAt;
}
