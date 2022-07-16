package cs.hku.hktransportandroid

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.IBinder
import androidx.lifecycle.*
import cs.hku.hktransportandroid.Entity.group
import cs.hku.hktransportandroid.repository.APIRepository
import cs.hku.hktransportandroid.util.minutesFromNow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RouteTimerService: LifecycleService() {
    @Inject
    lateinit var apiRepository: APIRepository
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val onStartResult =super.onStartCommand(intent, flags, startId)
        if (intent?.getBooleanExtra("DISMISS",false)==true){
            stopSelf()
            return onStartResult
        }
        val route = intent?.getStringExtra("route")
        val stop = intent?.getStringExtra("stop")
        val stopName = intent?.getStringExtra("stopName")
        if (route==null||stop==null||stopName==null){
            stopSelf()
            return onStartResult
        }

        val pendingIntent = PendingIntent.getService(this,0,Intent(this,RouteTimerService::class.java).apply {
                                                                                                             putExtra("DISMISS",true)
        },PendingIntent.FLAG_UPDATE_CURRENT)
        val notification: Notification = Notification.Builder(this, "CHANNEL_ID")
            .setContentTitle("$stopName $route")
            .setContentText("Next bus in")
            .setSmallIcon(R.drawable.ic_launcher_background)
            //.setContentIntent(pendingIntent)
            //.setTicker("ticker")
            .addAction(Notification.Action.Builder(null,"Dismiss",pendingIntent).build())
            .build()
        lifecycleScope.launchWhenCreated {
            do {
                val routeEta = apiRepository.getStopEta(stop).group(stop).find {
                    it.route == route
                }?.arrivalTime?.map { it.minutesFromNow() }?.sorted()
                updateNotification("$stopName $route","Next bus in ${routeEta?.firstOrNull()} Minutes")
            }while (isActive)
        }
        startForeground(1,notification )

        return onStartResult
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun updateNotification(title:String,content:String){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = PendingIntent.getService(this,0,Intent(this,RouteTimerService::class.java).apply {
            putExtra("DISMISS",true)
        },PendingIntent.FLAG_UPDATE_CURRENT)
        val notification: Notification = Notification.Builder(this, "CHANNEL_ID")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_background)
            //.setContentIntent(pendingIntent)
            //.setTicker("ticker")

            .addAction(Notification.Action.Builder(null,"Dismiss",pendingIntent).build())
            .build()
        notificationManager.notify(1,notification)
    }
}
