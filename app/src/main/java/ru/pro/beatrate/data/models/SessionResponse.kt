package ru.pro.beatrate.data.models

import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    val id: Long,
    val username: String,
    val date: String,
    val time: String,
    val arrangements: Boolean,
    val mixing_and_mastering: Boolean,
    val vocal_recording: Boolean,
    val neumann: Boolean,
    val shuresm: Boolean,
    val status: String,
    val duration: Int
)