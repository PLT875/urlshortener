package com.example.presentation.controller;

import com.example.domain.UrlService;
import com.example.domain.exception.UrlNotFoundException;
import com.example.presentation.controller.dto.CreateUrlRequestDto;
import com.example.presentation.controller.dto.CreateUrlResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UrlController {
    private final UrlService urlService;

    @PostMapping(path = "/v1/url", consumes = "application/json", produces = "application/json")
    ResponseEntity<CreateUrlResponseDto> shorten(@RequestBody CreateUrlRequestDto shortenUrlRequestDto) {
        return ResponseEntity.ok(CreateUrlResponseDto
                .from(urlService.shorten(shortenUrlRequestDto.url())));
    }

    @DeleteMapping(path = "/v1/url/{key}")
    ResponseEntity<Void> delete(@PathVariable String key) {
        urlService.delete(key);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handleUrlNotFound(UrlNotFoundException exception) {
        log.debug("Error", exception);
    }
}