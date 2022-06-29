@file:OptIn(ExperimentalFoundationApi::class)

package cs.hku.hktransportandroid.screen

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import cs.hku.hktransportandroid.RouteViewModel
import cs.hku.hktransportandroid.repository.Stop
import cs.hku.hktransportandroid.util.minutesFromNow
import cs.hku.hktransportandroid.util.toTime


@Composable
fun Route(navController: NavController, routeViewModel: RouteViewModel) {
    val route = routeViewModel.route.collectAsState(initial = null)
    val selectedIndex = routeViewModel.selectedIndex.collectAsState(initial = 0)
    val routeStops = routeViewModel.routeStop.collectAsState(initial = null)
    val cameraPositionState = rememberCameraPositionState {
        //TST
        position = CameraPosition.fromLatLngZoom(LatLng(22.29735258383955, 114.17205794244728), 17f)
    }
    if (routeStops.value != null) {
        cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(LatLngBounds.builder().apply {
            routeStops.value?.forEach {
                include(LatLng(it.lat.toDouble(), it.long.toDouble()))
            }
        }.build(), with(LocalDensity.current) { 24.dp.roundToPx() }))
    }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "${route.value?.route.orEmpty()} To: ${route.value?.destTc}")
        })
    }) { paddingValue ->
        Column() {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                cameraPositionState = cameraPositionState
            ) {
                routeStops.value?.mapIndexed { i, stop ->
                    NumberedMarker(
                        number = i + 1,
                        lat = stop.lat.toDouble(),
                        lng = stop.long.toDouble()
                    )
                }
                route.value?.lineGeometry?.let { pointsList->
                    Polyline(points =pointsList.map {  LatLng(it[1],it[0]) })
                }
            }
            Column(modifier = Modifier
                .padding(paddingValue)
                .verticalScroll(ScrollState(0))) {
                routeStops.value?.mapIndexed {i,stop-> RouteStopEta(i,stop) }
            }
        }
    }

}

@Composable
fun RouteStopEta(i: Int, stop: Stop) {
    val toggle = remember {
        mutableStateOf(false)
    }
    val canToggle = (stop.eta?.size ?: 0) > 1
    Column(modifier =
    Modifier.combinedClickable(
        enabled = canToggle ,
        onClick = {
            if (canToggle)
                toggle.value = !toggle.value
        },
    )) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp), verticalAlignment = CenterVertically
        ) {
            Canvas(modifier = Modifier
                .height(30.dp)
                .width(30.dp), onDraw = {
                drawIntoCanvas {
                    drawCircle(Color.Blue)
                    val textPaint = android.graphics.Paint().apply {
                        textSize = 60f
                        color = android.graphics.Color.WHITE
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    val textBound = Rect()
                    val text = (i + 1).toString()
                    textPaint.getTextBounds(text, 0, text.length, textBound)
                    it.nativeCanvas.drawText(
                        text,
                        size.width / 2,
                        size.height - (size.height - textBound.height()) / 2,
                        textPaint
                    )
                }
            })
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = stop.nameTc)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.padding(end = if (!canToggle) 16.dp else 0.dp),
                fontSize = 30.sp,
                text = stop.eta?.firstOrNull()?.eta?.toTime()?.minutesFromNow()?.toString()?:"-"
            )
            if (canToggle)
                Icon(
                    modifier = Modifier
                        .width(16.dp)
                        .height(16.dp),
                    imageVector = if (toggle.value) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = ""
                )
        }
        if (toggle.value) {
            stop.eta?.drop(1)?.map {
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 32.dp)
                ) {
                    Text(
                        fontSize = 30.sp,
                        text = it.eta?.toTime()?.minutesFromNow().toString()
                    )
                }
            }
        }
    }

}

@Composable
fun NumberedMarker(number: Int, lat: Double, lng: Double) {
    val height = with(LocalDensity.current) { 20.dp.roundToPx() }
    val width = with(LocalDensity.current) { 20.dp.roundToPx() }
    val bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    val paint = android.graphics.Paint()
    paint.color = android.graphics.Color.BLUE
    canvas.drawCircle(width / 2f, height / 2f, height / 2f, paint)
    val textPaint = android.graphics.Paint().apply {
        textSize = 20f
        color = android.graphics.Color.WHITE
        textAlign = android.graphics.Paint.Align.CENTER
    }
    val textBound = Rect()
    val text = (number).toString()
    textPaint.getTextBounds(text, 0, text.length, textBound)
    canvas.drawText(
        text,
        width / 2f,
        height - (height - textBound.height()) / 2f,
        textPaint
    )

    Marker(state = MarkerState(LatLng(lat, lng)), icon = BitmapDescriptorFactory.fromBitmap(bitmap))

}