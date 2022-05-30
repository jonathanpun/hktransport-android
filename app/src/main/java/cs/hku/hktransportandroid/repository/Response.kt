package cs.hku.hktransportandroid.repository
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class StopEta(
    @Json(name = "co")
    val co: String,
    @Json(name = "data_timestamp")
    val dataTimestamp: String,
    @Json(name = "dest_en")
    val destEn: String,
    @Json(name = "dest_sc")
    val destSc: String,
    @Json(name = "dest_tc")
    val destTc: String,
    @Json(name = "dir")
    val dir: String,
    @Json(name = "eta")
    val eta: String?,
    @Json(name = "eta_seq")
    val etaSeq: Int,
    @Json(name = "rmk_en")
    val rmkEn: String,
    @Json(name = "rmk_sc")
    val rmkSc: String,
    @Json(name = "rmk_tc")
    val rmkTc: String,
    @Json(name = "route")
    val route: String,
    @Json(name = "seq")
    val seq: Int,
    @Json(name = "service_type")
    val serviceType: Int
)

@JsonClass(generateAdapter = true)
data class Stop(
    @Json(name = "lat")
    val lat: String,
    @Json(name = "long")
    val long: String,
    @Json(name = "name_en")
    val nameEn: String,
    @Json(name = "name_sc")
    val nameSc: String,
    @Json(name = "name_tc")
    val nameTc: String,
    @Json(name = "stop")
    val stop: String
)

@JsonClass(generateAdapter = true)
data class Route(
    @Json(name = "bound")
    val bound: String,
    @Json(name = "dest_en")
    val destEn: String,
    @Json(name = "dest_sc")
    val destSc: String,
    @Json(name = "dest_tc")
    val destTc: String,
    @Json(name = "orig_en")
    val origEn: String,
    @Json(name = "orig_sc")
    val origSc: String,
    @Json(name = "orig_tc")
    val origTc: String,
    @Json(name = "route")
    val route: String,
    @Json(name = "service_type")
    val serviceType: String
)