package com.notarius.shortener.service;

import com.google.common.hash.Hashing;
import com.notarius.shortener.dto.UrlDto;
import com.notarius.shortener.entity.Url;
import com.notarius.shortener.exception.AlreadyExistException;
import com.notarius.shortener.exception.NewShortenerUrlNotIdenticalException;
import com.notarius.shortener.exception.NotFoundException;
import com.notarius.shortener.exception.ShortenerUrlToLongException;
import com.notarius.shortener.mapper.UrlMapper;
import com.notarius.shortener.mapper.UrlMapperImpl;
import com.notarius.shortener.repository.UrlShortenerRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {
    private static final int MAX_SHORTEN_URL_LENGTH = 10;
    private static final String CANT_BE_NULL = "Input cannot be null";
    private static final String ORIGINAL_URL = "originalUrl";
    private static final String URL = "URL";

    @Autowired
    private UrlShortenerRepository urlShortenerRepository;
    private UrlMapper urlMapper = new UrlMapperImpl();

    @Override
    @Transactional
    public UrlDto saveShortenUrl(Url url) {
        validateInput(url);
        String originalUrl = url.getOriginalUrl();
        String newShortenUrl = generateShortenUrl(originalUrl);

        validateNewShortenUrl(originalUrl, newShortenUrl);

        saveUrlWithShortenUrl(url, newShortenUrl);

        return urlMapper.toV1Resource(url);
    }


    @Override
    public UrlDto getUrlByShortenedUrl(String shortenedUrl) {
        Objects.requireNonNull(shortenedUrl, CANT_BE_NULL);
        return urlMapper.toV1Resource(
                urlShortenerRepository.findByShortenedUrl(shortenedUrl)
                        .orElseThrow(() -> new NotFoundException(Url.class,URL , shortenedUrl))
        );
    }

    @Override
    public List<UrlDto> listAll() {
        return urlShortenerRepository.findAll().stream()
                .map(urlDto -> urlMapper.toV1Resource(urlDto)).toList();
    }

    private void validateInput(Url url) {
        Objects.requireNonNull(url, CANT_BE_NULL);
        Objects.requireNonNull(url.getOriginalUrl(), CANT_BE_NULL);
    }

    private String generateShortenUrl(String originalUrl) {
        return Hashing.murmur3_32().hashString(originalUrl, Charset.defaultCharset()).toString();
    }

    private void validateNewShortenUrl(String originalUrl, String newShortenUrl) {
        urlShortenerRepository.findByOriginalUrl(originalUrl)
                .ifPresent(existingUrl -> {
                    validateShortenUrlIdentical(existingUrl.getShortenedUrl(), newShortenUrl);
                    throw new AlreadyExistException(ORIGINAL_URL, originalUrl);
                });
    }

    private void validateShortenUrlIdentical(String existingShortenUrl, String newShortenUrl) {
        if (null != existingShortenUrl && !existingShortenUrl.equals(newShortenUrl)) {
            throw new NewShortenerUrlNotIdenticalException(existingShortenUrl);
        }
    }

    private void validateShortenUrlLength(String newShortenUrl) {
        if (newShortenUrl.length() > MAX_SHORTEN_URL_LENGTH) {
            throw new ShortenerUrlToLongException(newShortenUrl);
        }
    }

    private void saveUrlWithShortenUrl(Url url, String newShortenUrl) {
        validateShortenUrlLength(newShortenUrl);
        url.setShortenedUrl(newShortenUrl);
        url.setCreatedAt(LocalDateTime.now());
        urlShortenerRepository.save(url);
    }

    @Override
    public void deleteAll() {
        urlShortenerRepository.deleteAll();
    }
}
