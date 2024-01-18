package com.notarius.shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlDto {
    private Long id;
    private String originalUrl;

    private String shortenedUrl;
}
