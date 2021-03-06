package cs.hku.hktransportandroid.screen

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BusAlert
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import cs.hku.hktransportandroid.HomeViewModel
import cs.hku.hktransportandroid.MainActivity
import cs.hku.hktransportandroid.R
import cs.hku.hktransportandroid.RouteTimerService
import cs.hku.hktransportandroid.screen.view.StopEta
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.function.Consumer
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun Home(navController: NavController, viewModel: HomeViewModel) {
    val rememberCoroutineScope = rememberCoroutineScope()
    val currentModelRouteState = remember { mutableStateOf("") }
    val currentModelStopState = remember { mutableStateOf("") }
    val currentModelRouteNameState = remember { mutableStateOf("") }
    val savedPointMap = viewModel.savedPoint.collectAsState(initial = null)
    val nearPointMap = viewModel.nearStops.collectAsState(initial = null)
    val modelBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    val lifeCycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    DisposableEffect(lifeCycleOwner.lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START)
                locationPermissionState.launchPermissionRequest()
            if (event==Lifecycle.Event.ON_CREATE)
                createNotificationChannel(context)
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
    LaunchedEffect(key1 = locationPermissionState.status) {
        if (locationPermissionState.status == PermissionStatus.Granted) {
            println("==================================")
            val location = locationAcquire(context)
            viewModel.onLocationUpdated(location.latitude,location.longitude)
        }
    }
    ModalBottomSheetLayout(sheetState = modelBottomSheetState, sheetContent = {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable {
                    viewModel.removeSavedPoint(
                        currentModelRouteState.value,
                        currentModelStopState.value
                    )
                    rememberCoroutineScope.launch {
                        modelBottomSheetState.hide()
                    }
                }) {
                Icon(
                    Icons.Filled.Add, modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(fontSize = 24.sp, text = "Remove")
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable {
                    val intent = Intent(context, RouteTimerService::class.java).apply {
                        putExtra("route",currentModelRouteState.value)
                        putExtra("stop",currentModelStopState.value)
                        putExtra("stopName",currentModelRouteNameState.value)
                    }
                    context.startService(intent)
                    rememberCoroutineScope.launch {
                        modelBottomSheetState.hide()
                    }
                }) {
                Icon(
                    Icons.Filled.NotificationAdd, modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(fontSize = 24.sp, text = "Track in notification")
            }
        }
    }) {
        Scaffold(topBar = { TopAppBar(title = { Text(text = "Home") }) }) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                if ((savedPointMap.value?.size ?: 0) > 0){
                    Text(text = "Saved Route", fontSize = 24.sp, modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp), textAlign = TextAlign.Center, color = Color.Gray)
                    Divider(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                }
                savedPointMap.value?.map { entry ->
                    Text(text = entry.key.nameTc)
                    entry.value.map {
                        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                            StopEta(stopEta = it, onLongClick = {
                                currentModelRouteState.value = it.route
                                currentModelStopState.value = entry.key.stop
                                currentModelRouteNameState.value = entry.key.nameTc
                                rememberCoroutineScope.launch {
                                    modelBottomSheetState.show()
                                }
                            }, Color.Black)
                        }
                    }
                }
                if ((savedPointMap.value?.size ?: 0) > 0)
                    Spacer(modifier = Modifier.height(12.dp))
                if ((nearPointMap.value?.size ?: 0) > 0){
                    Text(text = "Nearby", fontSize = 24.sp, modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp), textAlign = TextAlign.Center, color = Color.Gray)
                    Divider(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                }
                nearPointMap.value?.map { entry ->
                    Text(text = entry.key.nameTc)
                    entry.value.map {
                        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                            StopEta(stopEta = it, onLongClick = {}, Color.Black)
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
suspend fun locationAcquire(context: Context) = suspendCoroutine<Location> { cont ->
    val locationManager = (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
    val provider = locationManager.getProviders(Criteria(),true).let { it.find { it=="gps" }?:it.first() }
    locationManager.getCurrentLocation(provider,null,context.mainExecutor, Consumer {
        if (it!=null)
            cont.resume(it)
    })
}
fun createNotificationChannel(context: Context){
    // Create the NotificationChannel
    val name = context.getString(R.string.channel_name)
    val descriptionText = context.getString(R.string.channel_description)
    val importance = NotificationManager.IMPORTANCE_LOW
    val mChannel = NotificationChannel("CHANNEL_ID", name, importance)
    mChannel.description = descriptionText
    // Register the channel with the system; you can't change the importance
    // or other notification behaviors after this
    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(mChannel)
}
