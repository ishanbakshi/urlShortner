package com.example.urlShortner

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExtensionsTest {

    @Test
    fun `encodes string to Base32`() {
        val input = 34234234
        val expected = "GM2DEMZUGIZTI==="
        assertEquals(expected, input.encodeToBase32())
    }

    @Test
    fun `decodes Base32 to int`() {
        val input = "GM2DEMZUGIZTI==="
        val expected = 34234234
        assertEquals(expected, input.decodeFromBase32())
    }

}
