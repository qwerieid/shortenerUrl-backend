package com.notarius.shortener.mapper;

import com.notarius.shortener.dto.UrlDto;
import com.notarius.shortener.entity.Url;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UrlMapper {
    String DOMAIN_NAME = "https://www.notariusurl.com/";

    @Mapping(target = "id", source = "url.id")
    @Mapping(target = "originalUrl", source = "url.originalUrl")
    @Mapping(target = "shortenedUrl", expression = "java(getShortenedUrl(url))")
    UrlDto toV1Resource(Url url);

    default String getShortenedUrl(Url shortenedUrl) {
        return DOMAIN_NAME + shortenedUrl.getShortenedUrl();
    }

}