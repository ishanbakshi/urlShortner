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

@RestController
@RequestMapping("/v1/")
class UrlShortnerController {
    @GetMapping()
    fun getUrl(): ResponseEntity<Void> = ResponseEntity
        .status(HttpStatus.MOVED_PERMANENTLY)
        .header(HttpHeaders.LOCATION, "https://google.com")
        .build()

    @PostMapping()
    fun createShortUrl(@Valid @RequestBody body: UrlCreateRequest): Map<String, String> = mapOf("shortUrl" to "dummyURL1")
}