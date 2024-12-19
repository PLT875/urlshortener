package com.example.presentation.controller;

import com.example.domain.UrlService;
import com.example.domain.exception.UrlNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RedirectController {
    private final UrlService urlService;

    @GetMapping("/{key}")
    ResponseEntity<Void> redirectWithLocation(@PathVariable("key") String key) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.LOCATION, urlService.findLongUrl(key));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handleUrlNotFound(UrlNotFoundException exception) {
        log.debug("Error", exception);
    }
}
