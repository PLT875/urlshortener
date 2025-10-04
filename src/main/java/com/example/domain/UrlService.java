package com.example.domain;

import com.example.domain.configuration.DomainConfiguration;
import com.example.domain.exception.UrlNotFoundException;
import com.example.persistence.UrlRepository;
import com.example.persistence.entity.UrlEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import com.example.domain.model.Url;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UrlService {
    private static final String SHORT_URL_FORMAT = "%s://%s/%s";
    private final DomainConfiguration domainConfiguration;
    private final UrlRepository urlRepository;

    public Url getUrl(String id) {
        return urlRepository.findById(id)
                .map(urlEntity -> new Url(id, urlEntity.getLongUrl(), shortUrl(id)))
                .orElseThrow(() -> new UrlNotFoundException("URL not found"));
    }

    public Url createShortUrl(String longUrl) {
        String id = hash(longUrl);
        return urlRepository.findById(id)
                .map(urlEntity -> returnExistingOrResolveConflict(urlEntity, longUrl))
                .orElseGet(() ->  create(longUrl));
    }

    private Url create(String longUrl) {
        String id = hash(longUrl);
        Url url = new Url(id, longUrl, shortUrl(id));
        urlRepository.save(Url.toEntity(url));
        return url;
    }

    private Url returnExistingOrResolveConflict(UrlEntity urlEntity, String requestedLongUrl) {
        String existingLongUrl = urlEntity.getLongUrl();
        if (existingLongUrl.equals(requestedLongUrl)) {
            return new Url(urlEntity.getId(), requestedLongUrl, shortUrl(urlEntity.getId()));
        }
        String longUrl = urlEntity.getLongUrl();
        int offset = 1;
        String id;
        do {
            id = hash(longUrl + offset);
            offset++;
        }
        while (urlRepository.findById(id).isPresent());

        Url newUrl = new Url(id, requestedLongUrl, shortUrl(id));
        urlRepository.save(Url.toEntity(newUrl));
        return newUrl;
    }

    public void deleteShortUrl(String id) {
        urlRepository.findById(id)
                .ifPresentOrElse(
                        urlRepository::delete,
                        () -> { throw new UrlNotFoundException("URL not found"); });
    }

    private String hash(String url) {
        return DigestUtils.sha256Hex(url).substring(0, 7);
    }

    private String shortUrl(String id) {
        return format(SHORT_URL_FORMAT,
                domainConfiguration.getScheme(), domainConfiguration.getDomain(), id);
    }
}
