package com.notarius.shortener.service;

import com.notarius.shortener.dto.UrlDto;
import com.notarius.shortener.entity.Url;
import com.notarius.shortener.exception.AlreadyExistException;
import com.notarius.shortener.exception.NotFoundException;
import com.notarius.shortener.repository.UrlShortenerRepository;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlShortenerServiceImplIT {

    private static final String ORIGINAL_URL = "https://www.example.com";
    private static final String DOMAIN_URL = "https://www.notariusurl.com/";
    private static final String SHORTENER_URL = "6b30f676";

    @Autowired
    protected UrlShortenerService urlShortenerService;

    @Autowired
    protected UrlShortenerRepository urlShortenerRepository;

    protected Url url;

    @Before
    public void setUp() {
        url = Url.builder().originalUrl(ORIGINAL_URL).shortenedUrl(SHORTENER_URL).createdAt(LocalDateTime.now()).build();
        url = urlShortenerRepository.save(url);
    }

    @After
    public void tearDown() {
        urlShortenerRepository.deleteAll();
    }

    @Test
    public void testListAllUrls() {
        List<UrlDto> urlList = urlShortenerService.listAll();
        Assertions.assertThat(urlList).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void testSaveShortenedUrl() {
        Assertions.assertThat(url.getId()).isNotNull();
        Assertions.assertThat(url.getOriginalUrl()).isEqualTo(ORIGINAL_URL);

        Url fetchedUrl = urlShortenerRepository.findByShortenedUrl(SHORTENER_URL).orElse(null);
        Assertions.assertThat(fetchedUrl).isNotNull();
        Assertions.assertThat(fetchedUrl.getOriginalUrl()).isEqualTo(ORIGINAL_URL);
        Assertions.assertThat(url.getShortenedUrl()).isEqualTo(fetchedUrl.getShortenedUrl());
    }

    @Test(expected = AlreadyExistException.class)
    public void testSaveShortenedUrlAlreadyExistException() {
        Url duplicateUrl = Url.builder().originalUrl(ORIGINAL_URL).build();
        urlShortenerService.saveShortenUrl(duplicateUrl);
    }

    @Test
    public void testGetUrlByOriginalUrl() {
        UrlDto retrievedUrl = urlShortenerService.getUrlByShortenedUrl(SHORTENER_URL);
        Assertions.assertThat(retrievedUrl).isNotNull();
        Assertions.assertThat(retrievedUrl.getOriginalUrl()).isEqualTo(ORIGINAL_URL);
        Assertions.assertThat(retrievedUrl.getShortenedUrl()).isEqualTo(DOMAIN_URL + SHORTENER_URL);
    }

    @Test(expected = NotFoundException.class)
    public void testGetUrlByOriginalUrlNotFoundException() {
        urlShortenerService.getUrlByShortenedUrl("nonexistent-url");
    }
}