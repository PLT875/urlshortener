package com.example.domain;

import com.example.domain.configuration.DomainConfiguration;
import com.example.domain.exception.UrlNotFoundException;
import com.example.persistence.UrlRepository;
import com.example.persistence.dao.UrlDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.domain.model.Url;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final DomainConfiguration domainConfiguration;
    private final UrlRepository urlRepository;

    public Url shorten(String longUrl) {
        String id = new String(Base64.getEncoder()
                .encode(longUrl.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

        String shortUrl = format("%s://%s/%s",
                domainConfiguration.getScheme(), domainConfiguration.getDomain(), id);

        Url url = new Url(id, longUrl, shortUrl);
        urlRepository.save(Url.toUrlDao(url));
        return url;
    }

    public void delete(String id) {
        Optional<UrlDao> url = urlRepository.findById(id);
        if (url.isEmpty()) {
            throw new UrlNotFoundException("URL not found");
        }
        urlRepository.delete(url.get());
    }
}
