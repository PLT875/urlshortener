package com.example.domain;

import com.example.domain.configuration.DomainConfiguration;
import com.example.domain.exception.UrlNotFoundException;
import com.example.persistence.UrlRepository;
import com.example.persistence.dao.UrlDao;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import com.example.domain.model.Url;

import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final DomainConfiguration domainConfiguration;
    private final UrlRepository urlRepository;

    public String findLongUrl(String id) {
        return urlRepository.findById(id)
                .map(UrlDao::getLongUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found"));
    }

    public Url createShortUrl(String longUrl) {
        // simple shortening function for demo purposes (not production ready)
        // another widely documented method is a base62 encoding of its uniquely generated id (number)
        String id = DigestUtils.sha256Hex(longUrl).substring(0, 7);

        String shortUrl = format("%s://%s/%s",
                domainConfiguration.getScheme(), domainConfiguration.getDomain(), id);

        Url url = new Url(id, longUrl, shortUrl);
        urlRepository.save(Url.toDao(url));
        return url;
    }

    public void deleteShortUrl(String id) {
        Optional<UrlDao> url = urlRepository.findById(id);
        if (url.isEmpty()) {
            throw new UrlNotFoundException("URL not found");
        }
        urlRepository.delete(url.get());
    }
}
