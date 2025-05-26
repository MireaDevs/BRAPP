package ru.pro.beatrate.data.models

// Модель данных для сессии звукозаписи
data class Session(
    val username:String,
    val date: String,
    val time: String,
    val arrangements: Boolean,
    val mixing_and_mastering: Boolean,
    val vocal_recording: Boolean,
    val neumann:Boolean,
    val shuresm: Boolean,
    val status: String,
    val duration: Int,
)