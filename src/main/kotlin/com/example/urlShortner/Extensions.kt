package com.example.urlShortner

import org.apache.commons.codec.binary.Base32
import java.nio.charset.StandardCharsets


fun Int.encodeToBase32(): String {
    val base32 = Base32()
    val bytes = this.toString().toByteArray(StandardCharsets.UTF_8)
    return base32.encodeAsString(bytes)
}

fun String.decodeFromBase32(): String {
    val base32 = Base32()
    val decodedBytes = base32.decode(this)
    return String(decodedBytes, StandardCharsets.UTF_8)
}


