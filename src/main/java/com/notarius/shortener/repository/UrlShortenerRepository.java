package com.notarius.shortener.repository;

import com.notarius.shortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlShortenerRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortenedUrl(String originalUrl);

    Optional<Url> findByOriginalUrl(String originalUrl);

}
