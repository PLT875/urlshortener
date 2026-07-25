package com.example.adapter.in.rest;

import com.example.application.UrlService;
import com.example.domain.exception.UrlNotFoundException;
import com.example.adapter.in.rest.dto.CreateUrlRequestDto;
import com.example.adapter.in.rest.dto.CreateUrlResponseDto;
import com.example.adapter.in.rest.dto.GetUrlResponseDto;
import com.example.domain.model.Url;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UrlController {
    private final UrlService urlService;

    @Value("${url.shortener.app.scheme}")
    private String scheme;

    @Value("${url.shortener.app.domain}")
    private String domain;

    @GetMapping(path = "/v1/url/{key}", produces = "application/json")
    ResponseEntity<GetUrlResponseDto> getUrl(@PathVariable("key") String key) {
        Url url = urlService.getUrl(key);
        String shortUrl = formatShortUrl(url.id());
        return new ResponseEntity<>(new GetUrlResponseDto(url.id(), url.longUrl(), shortUrl), HttpStatus.OK);
    }

    @PostMapping(path = "/v1/url", consumes = "application/json", produces = "application/json")
    ResponseEntity<CreateUrlResponseDto> createShortUrl(@RequestBody @Valid CreateUrlRequestDto shortenUrlRequestDto) {
        Url url = urlService.createShortUrl(shortenUrlRequestDto.url());
        String shortUrl = formatShortUrl(url.id());
        return new ResponseEntity<>(new CreateUrlResponseDto(url.id(), url.longUrl(), shortUrl), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/v1/url/{key}")
    ResponseEntity<Void> deleteShortUrl(@PathVariable("key") String key) {
        urlService.deleteShortUrl(key);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handleUrlNotFound(UrlNotFoundException exception) {
        log.debug("Error", exception);
    }

    private String formatShortUrl(String id) {
        return format("%s://%s/%s", scheme, domain, id);
    }
}
