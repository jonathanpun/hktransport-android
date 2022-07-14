package cs.hku.hktransportandroid.repository

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("/stops-eta/{stopId}")
    suspend fun getStopEta(@Path("stopId") stopId:String):List<StopEta>
    @GET("/stops")
    suspend fun searchStop(@Query("q") query: String):List<StopTextSearch>
    @GET("/routes")
    suspend fun searchRoute(@Query("q")query: String,@Query("limit") limit:Int):List<Route>
    @GET("/stop/{stop}")
    suspend fun getStop(@Path("stop") stopId:String):Stop
    @GET("/routes/{route}/{bound}")
    suspend fun getRouteWithBound(@Path("route")route:String,@Path("bound")bound:String):List<Route>
    @GET("/routes/{route}/{bound}/{serviceType}")
    suspend fun getRouteWithBoundAndServiceType(@Path("route")route:String,@Path("bound")bound:String,@Path("serviceType")serviceType: String):Route
    @GET("/route-stops/{route}/{direction}/{service_type}")
    suspend fun getRouteStops(@Path("route")route:String,@Path("direction")direction:String,@Path("service_type")serviceType:String):List<Stop>
    @GET("/route-eta/{route}/{service_type}")
    suspend fun getRouteStopEta(@Path("route")route:String,@Path("service_type")serviceType:String):List<StopEta>
    @GET("/route-query")
    suspend fun getRecommendedRoute(@Query("sourceStop")sourceStop:Int, @Query("destStop")destStop:Int):List<RecommendedRoute>?
}