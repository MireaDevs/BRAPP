package ru.pro.beatrate.domain.beatrate_backend.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val role: String
)

