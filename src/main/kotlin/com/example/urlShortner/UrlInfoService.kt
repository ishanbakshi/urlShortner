package com.example.urlShortner

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Service
import java.rmi.server.UID
import java.util.UUID
import kotlin.random.Random

@Service
class UrlInfoService(private val db: JdbcTemplate) {

    fun findUrlInfoById(id: String): UrlInfo? = db.query("select * from url_info where id = ?", id.decodeFromBase32()) { response, _ ->
        UrlInfo(response.getString("id"), response.getString("fullUrl"))
    }.singleOrNull()

    fun createUrlInfo(fullUrl: String): String {

        val id = Random.nextInt() //TODO: Use a better ID generation mechanism which can serve short URLs
        db.update(
            "insert into url_info values ( ?, ? )",
            id, fullUrl
        )
        return id.encodeToBase32();
    }
}