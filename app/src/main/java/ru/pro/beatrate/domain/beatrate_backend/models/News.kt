package ru.pro.beatrate.domain.beatrate_backend.models

import kotlinx.serialization.Serializable

@Serializable
data class News(
    val title: String = "",        // безопасно по умолчанию
    val content: String = ""
)



