package ru.pro.beatrate.data.models
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val username: String = "",
    val date: String = "",
    val time: String = "",
    val arrangements: Boolean = false,
    val mixing_and_mastering: Boolean = false,
    val vocal_recording: Boolean = false,
    val neumann: Boolean = false,
    val shuresm: Boolean = false,
    val status: String = "",
    val duration: Int = 0
)