package cs.hku.hktransportandroid.repository

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("/stops-eta/{stopId}")
    suspend fun getStopEta(@Path("stopId") stopId:String):List<StopEta>
    @GET("/stops")
    suspend fun searchStop(@Query("q") query: String):List<Stop>
    @GET("/routes")
    suspend fun searchRoute(@Query("q")query: String,@Query("limit") limit:Int):List<Route>
    @GET("/stop/{stop}")
    suspend fun getStop(@Path("stop") stopId:String):Stop
}