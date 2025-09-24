package com.example.urlShortner

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class UrlShortnerController {
    @GetMapping()
    fun getDemo() = UrlInfo("http://localhost:8080/shorten/123456", "https://google.com")
}