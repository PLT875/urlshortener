# urlshortener

From the following challenge: https://codingchallenges.fyi/challenges/challenge-url-shortener

Used this opportunity to experiment with Testcontainers

# Requirements
* Java 21
* Docker

# Testing
```
./gradlew test
```

# Running
```
docker-compose up -d
./gradlew bootRun
```

# Examples

Shorten a URL
```
curl -X POST http://localhost:8080/v1/url \
--header 'Content-Type: application/json' \
--data-raw '{
    "url": "https://www.google.com"
}'
{"key":"ac6bb66","long_url":"https://www.google.com","short_url":"http://localhost:8080/ac6bb66"}
```

Redirect to long URL
```
curl -i http://localhost:8080/ac6bb66
HTTP/1.1 302
Location: https://www.google.com
Content-Length: 0
```

Delete a shortened URL
```
curl -X DELETE http://localhost:8080/v1/url/ac6bb66
```
