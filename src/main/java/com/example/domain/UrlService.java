package com.example.domain;

import com.example.domain.config.DomainConfiguration;
import com.example.domain.exception.UrlNotFoundException;
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
                .map(url -> new Url(url.id(), url.longUrl(), shortUrl(url.id())))
                .orElseThrow(() -> new UrlNotFoundException("URL not found"));
    }

    public Url createShortUrl(String longUrl) {
        String id = hash(longUrl);
        return urlRepository.findById(id)
                .map(existingUrl -> returnExistingOrResolveConflict(existingUrl, longUrl))
                .orElseGet(() -> create(longUrl));
    }

    private Url create(String longUrl) {
        String id = hash(longUrl);
        Url url = new Url(id, longUrl, shortUrl(id));
        urlRepository.save(url);
        return url;
    }

    private Url returnExistingOrResolveConflict(Url existingUrl, String requestedLongUrl) {
        String existingLongUrl = existingUrl.longUrl();
        if (existingLongUrl.equals(requestedLongUrl)) {
            return new Url(existingUrl.id(), requestedLongUrl, shortUrl(existingUrl.id()));
        }
        int offset = 1;
        String id;
        do {
            id = hash(existingLongUrl + offset);
            offset++;
        }
        while (urlRepository.findById(id).isPresent());

        Url newUrl = new Url(id, requestedLongUrl, shortUrl(id));
        urlRepository.save(newUrl);
        return newUrl;
    }

    public void deleteShortUrl(String id) {
        urlRepository.findById(id)
                .ifPresentOrElse(
                        url -> urlRepository.delete(id),
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
