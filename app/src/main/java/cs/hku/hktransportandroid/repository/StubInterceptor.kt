package cs.hku.hktransportandroid.repository

import android.content.Context
import android.content.res.AssetManager
import cs.hku.hktransportandroid.HKTransportApplication
import okhttp3.*

class StubInterceptor():Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val fileName = "${chain.request().method()}_${chain.request().url().pathSegments().joinToString(separator = "-")}.json"
        val responseJson = HKTransportApplication.applicationContext?.assets?.open(fileName)?.bufferedReader()?.use { it.readText()}
        return Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).message("").code(200).body(ResponseBody.create(MediaType.parse("application/json"),responseJson.orEmpty())).build()
    }
}