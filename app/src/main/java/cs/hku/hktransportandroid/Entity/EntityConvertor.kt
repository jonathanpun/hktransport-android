package cs.hku.hktransportandroid.Entity

import cs.hku.hktransportandroid.repository.StopEta
import cs.hku.hktransportandroid.util.toTime

fun List<StopEta>.group(): List<StopEtaGrouped> {
    return this.groupBy { it.route+it.serviceType }.map {
        StopEtaGrouped(
            route = it.value.first().route,
            destination = MultiLanguage(en = it.value.first().destEn, tc = it.value.first().destTc, sc = it.value.first().destSc),
            arrivalTime = it.value.sortedBy { it.etaSeq }.mapNotNull { it.eta?.toTime() }
        )
    }
}