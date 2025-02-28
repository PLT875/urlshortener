package com.example.domain;

import com.example.domain.configuration.DomainConfiguration;
import com.example.domain.exception.UrlNotFoundException;
import com.example.persistence.UrlRepository;
import com.example.persistence.entity.UrlEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import com.example.domain.model.Url;

import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UrlService {
    private static final String SHORT_URL_FORMAT = "%s://%s/%s";
    private final DomainConfiguration domainConfiguration;
    private final UrlRepository urlRepository;

    public Url getUrl(String id) {
        Optional<UrlEntity> url = urlRepository.findById(id);
        if (url.isEmpty()) {
            throw new UrlNotFoundException("URL not found");
        }
        return new Url(id, url.get().getLongUrl(), shortUrl(id));
    }

    public Url createShortUrl(String longUrl) {
        // simple shortening function for demo purposes (not production ready)
        // another widely documented method is a base62 encoding of its uniquely generated id (number)
        String id = DigestUtils.sha256Hex(longUrl).substring(0, 7);
        Url url = new Url(id, longUrl, shortUrl(id));
        urlRepository.save(Url.toEntity(url));
        return url;
    }

    public void deleteShortUrl(String id) {
        Optional<UrlEntity> url = urlRepository.findById(id);
        if (url.isEmpty()) {
            throw new UrlNotFoundException("URL not found");
        }
        urlRepository.delete(url.get());
    }

    private String shortUrl(String id) {
        return format(SHORT_URL_FORMAT,
                domainConfiguration.getScheme(), domainConfiguration.getDomain(), id);
    }
}
