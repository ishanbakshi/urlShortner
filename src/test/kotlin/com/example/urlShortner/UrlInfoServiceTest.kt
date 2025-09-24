package com.example.urlShortner

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UrlInfoServiceTest {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun `createUrlInfo should insert record and return UrlInfo with generated id`() {
        val service = UrlInfoService(jdbcTemplate)

        val fullUrl = "https://example.com/my/very/long/url"
        val created = service.createUrlInfo(fullUrl)

        // The ID should be a non-empty UUID string
        assertThat(created.id).isNotBlank()
        assertThat(created.fullUrl).isEqualTo(fullUrl)

        // Verify it was actually stored and can be read back
        val fetched = service.findUrlInfoById(created.id)
        assertThat(fetched).isNotNull
        assertThat(fetched!!.id).isEqualTo(created.id)
        assertThat(fetched.fullUrl).isEqualTo(fullUrl)
    }

    @Test
    fun `findUrlInfoById should return null when not found`() {
        val service = UrlInfoService(jdbcTemplate)

        val missing = service.findUrlInfoById("does-not-exist")
        assertThat(missing).isNull() //TODO: Throw 404 in this scenario when controller is integrated with this service.
    }

    @Test
    fun `createUrlInfo should enforce UNIQUE constraint on fullUrl`() {
        val service = UrlInfoService(jdbcTemplate)
        val fullUrl = "https://example.com/duplicate"

        // First insert succeeds
        service.createUrlInfo(fullUrl)

        // Second insert with same fullUrl should violate UNIQUE constraint
        assertThatThrownBy { service.createUrlInfo("https://example.com/duplicate") }
            .isInstanceOf(DataIntegrityViolationException::class.java)
    }
}
