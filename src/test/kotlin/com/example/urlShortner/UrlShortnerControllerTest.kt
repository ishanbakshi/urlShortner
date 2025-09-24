package com.example.urlShortner

import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content

@WebMvcTest(controllers = [UrlShortnerController::class])
class UrlShortnerControllerTest(@Autowired val mockMvc: MockMvc) {

    @Test
    @DisplayName("GET /v1/ should redirect permanently to https://google.com")
    fun getUrl_redirectsToGoogle() {
        mockMvc.perform(get("/v1/"))
            .andExpect(status().isMovedPermanently)
            .andExpect(header().string(HttpHeaders.LOCATION, "https://google.com"))
    }

    @Test
    @DisplayName("POST /v1/ should return the dummy short URL")
    fun createShortUrl_returnsDummy() {
        val body = """{"url":"https://example.com"}"""

        mockMvc.perform(
            post("/v1/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.shortUrl", equalTo("dummyURL1")))
    }

    @Test
    @DisplayName("POST /v1/ should validate payload to have mandatory field")
    fun createShortUrl_throwsValidationErrorOnMissingField() {
        val body = """{"foo":"https://example.com"}"""

        mockMvc.perform(
            post("/v1/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("POST /v1/ should validate url to be a valid URL")
    fun createShortUrl_throwsValidationErrorOnInvalidUrl() {
        val body = """{"url":"not-a-url"}"""

        mockMvc.perform(
            post("/v1/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("POST /v1/ should validate url to not be empty")
    fun createShortUrl_throwsValidationErrorOnEmptyUrl() {
        val body = """{"url":""}"""

        mockMvc.perform(
            post("/v1/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(status().isBadRequest)
    }
}
