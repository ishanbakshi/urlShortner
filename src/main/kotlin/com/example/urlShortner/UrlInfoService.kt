package com.example.urlShortner

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Service
import java.sql.Statement

@Service
class UrlInfoService(private val db: JdbcTemplate) {

    fun findUrlInfoById(id: String): UrlInfo? = db.query("select * from url_info where id = ?", id.decodeFromBase32()) { response, _ ->
        UrlInfo(response.getInt("id"), response.getString("fullUrl"))
    }.singleOrNull()

    fun createUrlInfo(fullUrl: String): String {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        db.update({ connection ->
            val ps = connection.prepareStatement(
                "insert into url_info (fullUrl) values (?)",
                Statement.RETURN_GENERATED_KEYS
            )
            ps.setString(1, fullUrl)
            ps
        }, keyHolder)

        val generatedId = keyHolder.key?.toInt() ?: throw IllegalStateException("Failed to retrieve generated id")
        return generatedId.encodeToBase32()
    }
}