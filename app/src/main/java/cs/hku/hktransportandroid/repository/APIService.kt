package cs.hku.hktransportandroid.repository

import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {
    @GET("/stops-eta/{stopId}")
    suspend fun getStopEta(@Path("stopId") stopId:String):List<StopEta>
}