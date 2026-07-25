package com.example.domain;

import com.example.domain.config.DomainConfiguration;
import com.example.domain.model.Url;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UrlServiceTest {
    @Mock
    private DomainConfiguration domainConfiguration;

    @Mock
    private UrlRepository urlRepository;

    private UrlService urlService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        urlService = new UrlService(domainConfiguration, urlRepository);
    }

    @Test
    void createShortUrl_isIdempotent() {
        // given
        String longUrl = "http://www.amazon.com";
        when(domainConfiguration.getScheme()).thenReturn("http");
        when(domainConfiguration.getDomain()).thenReturn("localhost:8888");
        Url url = urlService.createShortUrl(longUrl);
        reset(urlRepository);
        when(urlRepository.findById(url.id())).thenReturn(Optional.of(new Url(url.id(), longUrl, url.shortUrl())));
        // when
        Url actual = urlService.createShortUrl(longUrl);
        // then
        Url expected = new Url(url.id(), longUrl, "http://localhost:8888/" + url.id());
        assertThat(actual).isEqualTo(expected);
        verify(urlRepository, never()).save(any());
    }

    @Test
    void createShortUrl_handleCollisions() {
        // given
        String longUrl = "http://www.amazon.com";
        String id1 = DigestUtils.sha256Hex(longUrl).substring(0, 7);
        Url conflictedUrl1 = new Url(id1, "http://www.amazon.co.uk");
        String id2 = DigestUtils.sha256Hex(longUrl + 1).substring(0, 7);
        Url conflictedUrl2 = new Url(id2, "http://www.amazon.de");
        when(domainConfiguration.getScheme()).thenReturn("http");
        when(domainConfiguration.getDomain()).thenReturn("localhost:8888");
        when(urlRepository.findById(id1)).thenReturn(Optional.of(conflictedUrl1));
        when(urlRepository.findById(id2)).thenReturn(Optional.of(conflictedUrl2));
        // when
        Url actual = urlService.createShortUrl(longUrl);
        // then
        Url expected = new Url(actual.id(), longUrl, "http://localhost:8888/" + actual.id());
        assertThat(actual).isEqualTo(expected);
        verify(urlRepository, times(2)).findById(any());
        verify(urlRepository, times(1)).save(any());
    }

}

