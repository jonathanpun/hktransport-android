package cs.hku.hktransportandroid.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class APIRepository {
    val retrofit = Retrofit.Builder().baseUrl("http://192.168.0.195:8080")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level= HttpLoggingInterceptor.Level.BODY
        }).build())
        .build()
    private fun getService()= retrofit.create(APIService::class.java)

    suspend fun getStopEta(stopId:String): List<StopEta> =
        withContext(Dispatchers.IO){
            getService().getStopEta(stopId)
        }
    suspend fun getStop(q:String):List<Stop> =
        withContext(Dispatchers.IO){
            getService().getStop(q)
        }
}