package cs.hku.hktransportandroid.repository

import cs.hku.hktransportandroid.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class APIRepository @Inject constructor() {
    val retrofit = Retrofit.Builder().baseUrl("http://192.168.0.195:8080")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level= HttpLoggingInterceptor.Level.BODY
        }).apply {
            if (BuildConfig.FLAVOR.contains("stub"))
                addInterceptor(StubInterceptor())
        }.build())
        .build()
    private fun getService()= retrofit.create(APIService::class.java)

    suspend fun getStopEta(stopId:String): List<StopEta> =
        withContext(Dispatchers.IO){
            getService().getStopEta(stopId)
        }
    suspend fun searchStop(q:String):List<StopTextSearch> =
        withContext(Dispatchers.IO){
            getService().searchStop(q)
        }
    suspend fun searchRoute(q:String,limit:Int):List<Route> =
        withContext(Dispatchers.IO){
            getService().searchRoute(q,limit)
        }
    suspend fun getRouteWithBound(route:String,bound:String):List<Route> = withContext(Dispatchers.IO){
        getService().getRouteWithBound(route,bound)
    }
    suspend fun getRouteWithBoundAndServiceType(route:String,bound:String,serviceType:String)= withContext(Dispatchers.IO){
        getService().getRouteWithBoundAndServiceType(route,bound,serviceType)
    }
    suspend fun getStop(stopId:String):Stop =
        withContext(Dispatchers.IO){
            getService().getStop(stopId)
        }
    suspend fun getRouteStop(route:String,bound:String,serviceType:String) = withContext(Dispatchers.IO){
        getService().getRouteStops(route, bound, serviceType)
    }
    suspend fun getRouteEta(route:String,serviceType:String) = withContext(Dispatchers.IO){
        getService().getRouteStopEta(route, serviceType)
    }

    suspend fun queryRoute(sourceStop:Int,destStop:Int) = withContext(Dispatchers.IO){
        getService().getRecommendedRoute(sourceStop,destStop)
    }
}