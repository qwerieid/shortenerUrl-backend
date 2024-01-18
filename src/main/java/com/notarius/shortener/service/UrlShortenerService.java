package com.notarius.shortener.service;

import com.notarius.shortener.dto.UrlDto;
import com.notarius.shortener.entity.Url;

import java.util.List;

public interface UrlShortenerService {
     UrlDto getUrlByShortenedUrl(String shortenedUrl);
     UrlDto saveShortenUrl(Url url);

     List<UrlDto> listAll();

     void deleteAll();
}
