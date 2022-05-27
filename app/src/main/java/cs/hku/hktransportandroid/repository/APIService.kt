package cs.hku.hktransportandroid.repository

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("/stops-eta/{stopId}")
    suspend fun getStopEta(@Path("stopId") stopId:String):List<StopEta>
    @GET("/stops")
    suspend fun getStop(@Query("q") query: String):List<Stop>
}