package com.notarius.shortener.service;

import com.notarius.shortener.dto.UrlDto;
import com.notarius.shortener.entity.Url;
import com.notarius.shortener.exception.AlreadyExistException;
import com.notarius.shortener.exception.NewShortenerUrlNotIdenticalException;
import com.notarius.shortener.exception.NotFoundException;
import com.notarius.shortener.exception.ShortenerUrlToLongException;
import com.notarius.shortener.mapper.UrlMapper;
import com.notarius.shortener.repository.UrlShortenerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UrlShortenerServiceImplTest {

    private static final String ORIGINAL_URL = "https://www.nonexistent-url.com";
    private static final String SHORT_EXISTING_URL = "85a5a78d";
    private static final String SHORT_NON_EXISTING_URL = "test";
    private static final String VALIDATE_SHORTENER_URL_LENGTH = "validateShortenUrlLength";
    private static final String LONG_SHORTENER_URL = "toolongshortenedurl";
    private static final String SHORT_SHORTENER_URL = "short";

    @InjectMocks
    private UrlShortenerServiceImpl urlShortenerService;

    @Mock
    private UrlShortenerRepository urlShortenerRepositoryMock;

    @Mock
    private UrlMapper urlMapperMock;


    @Test
    void testSaveShortenUrl() {
        Url inputUrl = Url.builder().originalUrl(ORIGINAL_URL).build();
        UrlDto urlDto = new UrlDto();

        when(urlShortenerRepositoryMock.findByOriginalUrl(inputUrl.getOriginalUrl())).thenReturn(Optional.empty());
        when(urlShortenerRepositoryMock.save(inputUrl)).thenReturn(inputUrl);
        when(urlMapperMock.toV1Resource(inputUrl)).thenReturn(urlDto);

        UrlDto result = urlShortenerService.saveShortenUrl(inputUrl);

        assertNotNull(result);
        assertEquals(urlDto, result);

        verify(urlShortenerRepositoryMock, times(1)).findByOriginalUrl(inputUrl.getOriginalUrl());
        verify(urlShortenerRepositoryMock, times(1)).save(inputUrl);
    }

    @Test
    void testSaveShortenUrlNotIdenticalException() {
        Url inputUrl = Url.builder().originalUrl(ORIGINAL_URL).build();
        Url existingUrl = Url.builder().originalUrl(ORIGINAL_URL).shortenedUrl(SHORT_NON_EXISTING_URL).build();

        when(urlShortenerRepositoryMock.findByOriginalUrl(existingUrl.getOriginalUrl())).thenReturn(Optional.of(existingUrl));

        assertThrows(NewShortenerUrlNotIdenticalException.class, () -> urlShortenerService.saveShortenUrl(inputUrl));

        verify(urlShortenerRepositoryMock, times(1)).findByOriginalUrl(inputUrl.getOriginalUrl());
    }

    @Test
    void testSaveShortenUrlAlreadyExistException() {
        Url existingUrl = Url.builder().originalUrl(ORIGINAL_URL).build();
        Url inputUrl = Url.builder().originalUrl(ORIGINAL_URL).shortenedUrl(SHORT_EXISTING_URL).build();

        when(urlShortenerRepositoryMock.findByOriginalUrl(ORIGINAL_URL)).thenReturn(Optional.of(inputUrl));

        assertThrows(AlreadyExistException.class, () -> urlShortenerService.saveShortenUrl(existingUrl));

        verify(urlShortenerRepositoryMock, times(1)).findByOriginalUrl(inputUrl.getOriginalUrl());
        verify(urlShortenerRepositoryMock, never()).save(inputUrl);
    }

    @Test
    void testGetUrlByOriginalUrl() {
        Url url = Url.builder().originalUrl(ORIGINAL_URL).build();
        UrlDto urlDto = UrlDto.builder().originalUrl(ORIGINAL_URL).build();

        when(urlShortenerRepositoryMock.findByShortenedUrl(SHORT_SHORTENER_URL)).thenReturn(Optional.of(url));
        when(urlMapperMock.toV1Resource(any())).thenReturn(urlDto);

        UrlDto result = urlShortenerService.getUrlByShortenedUrl(SHORT_SHORTENER_URL);

        assertNotNull(result);
        assertEquals(url.getShortenedUrl(), result.getShortenedUrl());
        assertEquals(url.getOriginalUrl(), result.getOriginalUrl());
        verify(urlShortenerRepositoryMock, times(1)).findByShortenedUrl(SHORT_SHORTENER_URL);
    }

    @Test
    void testGetUrlByOriginalUrlNotFoundException() {
        when(urlShortenerRepositoryMock.findByShortenedUrl(SHORT_SHORTENER_URL)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> urlShortenerService.getUrlByShortenedUrl(SHORT_SHORTENER_URL));

        verify(urlShortenerRepositoryMock, times(1)).findByShortenedUrl(SHORT_SHORTENER_URL);
    }

    @Test
    void testValidateShortenUrlLength() {
        assertThrows(ShortenerUrlToLongException.class, () -> {
            ReflectionTestUtils.invokeMethod(urlShortenerService, VALIDATE_SHORTENER_URL_LENGTH, LONG_SHORTENER_URL);
        });
    }

    @Test
    void testValidateShortenUrlLength_KO() {
        assertDoesNotThrow(() -> {
            ReflectionTestUtils.invokeMethod(urlShortenerService, VALIDATE_SHORTENER_URL_LENGTH, SHORT_SHORTENER_URL);
        });
    }

    @Test
    void testListAll() {
        Url url1 = Url.builder().originalUrl(ORIGINAL_URL).build();
        List<Url> urlList = Arrays.asList(url1);

        UrlDto urlDto1 = UrlDto.builder().originalUrl(ORIGINAL_URL).build();
        List<UrlDto> urlDtoList = Arrays.asList(urlDto1);

        when(urlShortenerRepositoryMock.findAll()).thenReturn(urlList);
        when(urlMapperMock.toV1Resource(url1)).thenReturn(urlDto1);

        List<UrlDto> resultList = urlShortenerService.listAll();

        assertNotNull(resultList);
        assertEquals(urlList.size(), resultList.size());
        assertTrue(resultList.contains(urlDto1));

        verify(urlShortenerRepositoryMock, times(1)).findAll();
    }
}
