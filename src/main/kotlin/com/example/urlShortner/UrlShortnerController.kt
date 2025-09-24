package com.example.urlShortner

import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/v1/")
class UrlShortnerController ( private val urlInfoService: UrlInfoService ){
    @GetMapping()
    fun getUrl(): ResponseEntity<Void> = ResponseEntity
        .status(HttpStatus.MOVED_PERMANENTLY)
        .header(HttpHeaders.LOCATION, "https://google.com")
        .build()

    @PostMapping()
    fun createShortUrl(@Valid @RequestBody body: UrlCreateRequest): Map<String, String> {
        val created: UrlInfo = urlInfoService.createUrlInfo(body.url)
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString().trim('/') + "/v1/"
        // Return a concatenated short URL as specified (e.g., "https://localhost:8080/" + created.id)
        return mapOf("shortUrl" to (baseUrl + created.id))
    }
}