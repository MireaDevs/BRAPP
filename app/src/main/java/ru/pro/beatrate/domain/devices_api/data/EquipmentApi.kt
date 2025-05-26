package ru.pro.beatrate.domain.devices_api.data

import retrofit2.http.GET

interface EquipmentApi {
    @GET("equip/Equipment")
    suspend fun getEquipment(): List<EquipmentItem>
}