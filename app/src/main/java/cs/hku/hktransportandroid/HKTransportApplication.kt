package cs.hku.hktransportandroid

import android.app.Application
import android.content.Context

class HKTransportApplication:Application() {
    companion object{
        var applicationContext:Context? = null
    }
    init {
        HKTransportApplication.applicationContext= this
    }
}