package ru.pro.beatrate.domain.devices_api.data

class EquipmentRepository(
    private val api: EquipmentApi = RetrofitInstance.api
) {
    suspend fun fetchEquipment(): List<EquipmentItem> = api.getEquipment()
}