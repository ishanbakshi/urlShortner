package com.example.urlShortner

import org.apache.commons.codec.binary.Base32
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.nio.charset.StandardCharsets


fun Int.encodeToBase32(): String {
    val base32 = Base32()
    val bytes = this.toString().toByteArray(StandardCharsets.UTF_8)
    return base32.encodeAsString(bytes)
}

fun String.decodeFromBase32(): Int {
    val base32 = Base32()
    try {
        val decodedBytes = base32.decode(this)
        val decodedString = String(decodedBytes, StandardCharsets.UTF_8)
        return decodedString.toIntOrNull() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    } catch (ex: Exception) {
        // Any decoding or parsing issue should result in a 404
        throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}


