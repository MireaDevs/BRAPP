package ru.pro.beatrate.data.objects.booking

import ru.pro.beatrate.R
import ru.pro.beatrate.data.models.Service

object ConstantsOfServices {
    val ServiceItems = listOf(
        Service("Аранжировки", R.drawable.arrangements,null),
        Service("сведение и мастеринг", R.drawable.mixing_and_mastering,null),
        Service("Запись вокала", R.drawable.vocal_recording,null),
    )
}