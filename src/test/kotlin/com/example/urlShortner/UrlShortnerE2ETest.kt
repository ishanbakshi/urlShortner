package com.example.urlShortner

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.matchesPattern
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest // boots full context: controller + service + JDBC + H2
@AutoConfigureMockMvc
@Transactional // rollback after test; keeps DB clean between tests
class UrlShortnerE2ETest {

    @Autowired
    lateinit var mockMvc: MockMvc

    private val objectMapper = ObjectMapper()

    @Test
    @DisplayName("E2E: Create a short URL then resolve it via 301 redirect")
    fun createThenResolve() {
        val originalUrl = "https://example.com/some/page"

        // 1) Create short URL
        val createResult = mockMvc.perform(
            post("/v1/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"url":"$originalUrl"}""")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // ensure the value looks like http://localhost/v1/<something>
            .andExpect(jsonPath("$.shortUrl", matchesPattern("^http://localhost(:\\d+)?/v1/.+")))
            .andReturn()

        val body = createResult.response.contentAsString
        val json: JsonNode = objectMapper.readTree(body)
        val shortUrl = json.get("shortUrl").asText()

        // Extract only the path part for MockMvc (e.g., /v1/<id>)
        val shortPath = shortUrl.substringAfter("http://localhost").substringAfter(":")
            .ifEmpty { shortUrl.substringAfter("http://localhost") } // handle case with no port in string

        val pathToResolve = if (shortPath.startsWith("/")) shortPath else "/$shortPath"

        // 2) Resolve short URL (should redirect 301 to original)
        mockMvc.perform(get(pathToResolve))
            .andExpect(status().isMovedPermanently)
            .andExpect(header().string(HttpHeaders.LOCATION, originalUrl))
    }

    @Test
    @DisplayName("E2E: GET unknown id returns 404")
    fun resolveUnknown_returns404() {
        mockMvc.perform(get("/v1/does-not-exist"))
            .andExpect(status().isNotFound)
    }
}