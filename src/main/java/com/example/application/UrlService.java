package com.example.application;

import com.example.domain.exception.UrlNotFoundException;
import com.example.domain.port.in.UrlUseCase;
import com.example.domain.port.out.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import com.example.domain.model.Url;

@Service
@RequiredArgsConstructor
public class UrlService implements UrlUseCase {
    private final UrlRepository urlRepository;

    public Url getUrl(String id) {
        return urlRepository.findById(id)
                .map(url -> new Url(url.id(), url.longUrl()))
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
        Url url = new Url(id, longUrl);
        urlRepository.save(url);
        return url;
    }

    private Url returnExistingOrResolveConflict(Url existingUrl, String requestedLongUrl) {
        String existingLongUrl = existingUrl.longUrl();
        if (existingLongUrl.equals(requestedLongUrl)) {
            return new Url(existingUrl.id(), requestedLongUrl);
        }
        int offset = 1;
        String id;
        do {
            id = hash(existingLongUrl + offset);
            offset++;
        }
        while (urlRepository.findById(id).isPresent());

        Url newUrl = new Url(id, requestedLongUrl);
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

}
