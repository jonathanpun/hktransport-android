package cs.hku.hktransportandroid.Entity

import java.time.LocalDateTime

data class StopEtaGrouped(
    val stop:String,
    val route:String,
    val destination:MultiLanguage,
    val arrivalTime:List<LocalDateTime>
)