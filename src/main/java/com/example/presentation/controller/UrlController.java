package com.example.presentation.controller;

import com.example.domain.UrlService;
import com.example.domain.exception.UrlNotFoundException;
import com.example.presentation.controller.dto.CreateUrlRequestDto;
import com.example.presentation.controller.dto.CreateUrlResponseDto;
import com.example.presentation.controller.dto.GetUrlResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UrlController {
    private final UrlService urlService;

    @GetMapping(path = "/v1/url/{key}", produces = "application/json")
    CompletionStage<ResponseEntity<GetUrlResponseDto>> getUrl(@PathVariable("key") String key) {
        return urlService.getUrl(key)
                .thenApply(url -> new ResponseEntity<>(GetUrlResponseDto.from(url), HttpStatus.OK));
    }

    @PostMapping(path = "/v1/url", consumes = "application/json", produces = "application/json")
    CompletionStage<ResponseEntity<CreateUrlResponseDto>> createShortUrl(@RequestBody @Valid CreateUrlRequestDto shortenUrlRequestDto) {
        return urlService.createShortUrl(shortenUrlRequestDto.url())
                .thenApply(url -> new ResponseEntity<>(CreateUrlResponseDto.from(url), HttpStatus.CREATED));
    }

    @DeleteMapping(path = "/v1/url/{key}")
    CompletionStage<ResponseEntity<Void>> deleteShortUrl(@PathVariable("key") String key) {
        return urlService.deleteShortUrl(key)
                .thenApply(v -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @ExceptionHandler(UrlNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handleUrlNotFound(UrlNotFoundException exception) {
        log.debug("Error", exception);
    }
}
