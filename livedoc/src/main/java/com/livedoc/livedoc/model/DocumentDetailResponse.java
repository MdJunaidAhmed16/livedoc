package com.livedoc.livedoc.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentDetailResponse {
    
    private UUID id;
    private String title;
    private String content;
    private Long updateAt;
}
