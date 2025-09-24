package com.example.urlShortner

import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
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

    @MockitoBean
    lateinit var urlInfoService: UrlInfoService

    @Test
    @DisplayName("GET /v1/{id} should redirect permanently to the stored fullUrl")
    fun getUrl_redirectsToStoredUrl() {
        Mockito.`when`(urlInfoService.findUrlInfoById("abc555")).thenReturn(
            UrlInfo(id = "abc555", fullUrl = "https://example.com/page")
        )

        mockMvc.perform(get("/v1/abc555"))
            .andExpect(status().isMovedPermanently)
            .andExpect(header().string(HttpHeaders.LOCATION, "https://example.com/page"))
    }

    @Test
    @DisplayName("GET /v1/{id} should return 404 when id not found")
    fun getUrl_returnsNotFoundWhenMissing() {
        Mockito.`when`(urlInfoService.findUrlInfoById("missing")).thenReturn(null)

        mockMvc.perform(get("/v1/missing"))
            .andExpect(status().isNotFound)
    }

    @Test
    @DisplayName("POST /v1/ should return the concatenated short URL with host")
    fun createShortUrl_returnsConcatenatedUrl() {
        val body = """{"url":"https://example.com"}"""

        // Mock the service to return a deterministic id
        Mockito.`when`(urlInfoService.createUrlInfo("https://example.com")).thenReturn(
            UrlInfo(id = "abc123", fullUrl = "https://example.com")
        )

        mockMvc.perform(
            post("/v1/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.shortUrl", equalTo("http://localhost/v1/abc123")))
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
