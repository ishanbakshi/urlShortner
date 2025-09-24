package com.example.urlShortner

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UrlInfoService(private val db: JdbcTemplate) {

    fun findUrlInfoById(id: String): UrlInfo? = db.query("select * from url_info where id = ?", id) { response, _ ->
        UrlInfo(response.getString("id"), response.getString("fullUrl"))
    }.singleOrNull()

    fun createUrlInfo(fullUrl: String): UrlInfo {
        //TODO : fetch the URL if it already exists and return the existing URL info instead of creating a new one in the DB
        val id = UUID.randomUUID().toString() //TODO: Use a better ID generation mechanism which can serve short URLs
        db.update(
            "insert into url_info values ( ?, ? )",
            id, fullUrl
        )
        return UrlInfo(id, fullUrl)
    }
}