package com.notarius.shortener.controller;

import com.notarius.shortener.dto.UrlDto;
import com.notarius.shortener.entity.Url;
import com.notarius.shortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "urls")
@CrossOrigin("*")
public class UrlController {
    @Autowired
    private UrlShortenerService urlShortenerService;

    @GetMapping
    public ResponseEntity<List<UrlDto>> listAll() {
        return ResponseEntity.status(HttpStatus.OK).body(urlShortenerService.listAll());

    }

    @GetMapping("/{shortenedUrl}")
    public ResponseEntity<UrlDto> getUrlByShortenerUrl(@PathVariable String shortenedUrl) {
        return ResponseEntity.status(HttpStatus.OK).body(urlShortenerService.getUrlByShortenedUrl(shortenedUrl));
    }

    @PostMapping
    public ResponseEntity<UrlDto> createUrl(@RequestBody Url url) {
        return ResponseEntity.status(HttpStatus.CREATED).body(urlShortenerService.saveShortenUrl(url));
    }

    @PostMapping("/deleteAll")
    public ResponseEntity<Void> deleteAll() {
        urlShortenerService.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
