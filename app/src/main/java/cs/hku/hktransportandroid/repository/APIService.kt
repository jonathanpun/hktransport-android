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
    @GET("/route-stop/{route}/{direction}/{service_type}")
    suspend fun getRouteStops(@Path("route")route:String,@Path("direction")direction:String,@Path("service_type")serviceType:String)
    @GET("/route-eta/{route}/{service_type}")
    suspend fun getRouteStop(@Path("route")route:String,@Path("service_type")serviceType:String)

}