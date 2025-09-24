package com.example.urlShortner

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.URL

data class UrlCreateRequest(
    @field:NotBlank(message = "url must not be blank")
    @field:URL(message = "url must be a valid URL")
    val url: String
)
