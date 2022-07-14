package cs.hku.hktransportandroid.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import cs.hku.hktransportandroid.PlanViewModel
import cs.hku.hktransportandroid.repository.RecommendedRoute
import java.lang.Math.abs

@Composable
fun Plan(navController: NavController,vm:PlanViewModel) {
    val sourceStopText = vm.sourceStopText.collectAsState(initial = "")
    val destStopText = vm.destinationStopText.collectAsState("")
    val sourceStop  = vm.sourStop.collectAsState(initial = null)
    val destStop = vm.destinationStop.collectAsState(initial = null)
    val searchStopQueryResultList = vm.searchStopsResult.collectAsState(initial = null)
    val recommendedRouteDisplay = vm.recommendedRouteDisplay.collectAsState(null)
//    val cameraPositionState = rememberCameraPositionState {
//        //TST
//        position = CameraPosition.fromLatLngZoom(LatLng(22.29735258383955, 114.17205794244728), 17f)
//    }
//    if (recommendedRouteDisplay.value != null){
//        cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(LatLngBounds.builder().apply {
//            recommendedRouteDisplay.value?.recommendedRoute?.routeStops?.flatMap { it.stops }?.forEach {
//                include(LatLng(it.lat.toDouble(), it.long.toDouble()))
//            }
//        }.build(), with(LocalDensity.current) { 24.dp.roundToPx() }))
//    }
    Scaffold(topBar = { TopAppBar(title = { Text("Planning") }) }) {
        Column(modifier = Modifier.padding(it)) {
            Box(modifier = Modifier
                .padding(4.dp)
                .border(1.dp, color = Color.Black, shape = CutCornerShape(4.dp))) {
                Column {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 4.dp),
                        placeholder = { Text("Source Stop") },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.PinDrop,
                                contentDescription = ""
                            )
                        },
                        trailingIcon =  { if(sourceStop.value!=null)
                            Icon(Icons.Filled.Clear,
                                contentDescription = "clear",
                                modifier = Modifier
                                    .clickable {
                                        vm.onSourceStopClear()
                                    }
                            )},
                        value = sourceStop.value?.nameTc?:sourceStopText.value ,
                        colors=TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent),
                        onValueChange = {
                            vm.onSourceStopTextChanged(it)
                        },
                        readOnly = sourceStop.value!=null,
                    )
                    Divider(color = Color.Black)
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 4.dp),
                        placeholder = { Text("Destination") },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.PinDrop,
                                contentDescription = ""
                            )
                        },
                        trailingIcon =  {
                            if(destStop.value!=null)
                                Icon(Icons.Filled.Clear,
                                    contentDescription = "clear",
                                    modifier = Modifier
                                        .clickable {
                                            vm.onDestinationStopClear()
                                        }
                                )},
                        colors=TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent),
                        value = destStop.value?.nameTc?:destStopText.value,
                        onValueChange = {
                            vm.onDestinationStopTextChanged(it)
                        },
                        readOnly = destStop.value!=null,
                    )
                }
            }
            if (searchStopQueryResultList.value!=null)
            Box {
                Column {
                    Text(
                        text = "Stop",
                        modifier = Modifier.padding(start = 16.dp),
                        fontSize = 20.sp
                    )
                    searchStopQueryResultList.value?.stops?.map {
                        Text(text = it.nameTc, modifier = Modifier
                            .padding(start = 24.dp)
                            .fillMaxWidth()
                            .clickable {
                                vm.onStopSelect(searchStopQueryResultList.value?.type!!, it)
                            }, fontSize = 16.sp
                        )
                    }

                }
            }
//            val colors = arrayOf(Color.Black,Color.Red,Color.Blue,Color.Gray)
//            GoogleMap(cameraPositionState = cameraPositionState,modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp),){
//                val routeStops = recommendedRouteDisplay.value?.recommendedRoute?.routeStops
//                routeStops?.mapIndexed {i, it->
//                    val startStop = it.stops.first()
//                    val endStop = it.stops.last()
//                    val threshold = 0.0005
//                    val startIndex = it.route.lineGeometry?.indexOfFirst {
//                        abs(it[1]-startStop.lat.toDouble())< threshold && abs(it[0]-startStop.long.toDouble())<threshold
//                    }
//                    val endIndex = it.route.lineGeometry?.indexOfFirst { abs(it[1]-endStop.lat.toDouble())<threshold  && abs(it[0]-endStop.long.toDouble())<threshold}
//                    if (startIndex==null ||startIndex == -1||endIndex==null || endIndex== -1)
//                        return@mapIndexed
//                    //Polyline(points = it.route.lineGeometry.subList(0,it.route.lineGeometry.size).map { LatLng(it[1],it[0]) }, color = colors[i%colors.size])
//                    val subList = it.route.lineGeometry.subList(if (startIndex<=endIndex)startIndex else endIndex, if (startIndex<= endIndex )endIndex+1 else startIndex+1)
//                    Polyline(points = subList.map { LatLng(it[1],it[0]) }, color = colors[i%colors.size])
//                }
//                recommendedRouteDisplay.value?.recommendedRoute?.routeStops?.mapIndexed {i,it->
//                    NumberedMarker(i+1,it.stops.first().lat.toDouble(),it.stops.first().long.toDouble())
//                }
//            }
            recommendedRouteDisplay.value?.recommendedRoutes?.map {
                FlowRow( modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp), crossAxisAlignment = FlowCrossAxisAlignment.Center) {
                        it.routeStops.map {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(text = it.route.route, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "To: ${it.route.destTc}", fontSize = 12.sp)
                                }
                                Text(text = it.stops.first().nameTc,fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(24.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top=12.dp)) {
                                Canvas(modifier = Modifier
                                    .height(5.dp)
                                    .width(24.dp),
                                    onDraw = {
                                        drawCircle(Color.Blue, radius = 5f, center = Offset(size.width*0.25f, size.height *0.9f))
                                        drawCircle(Color.Blue, radius = 5f, center = Offset(size.width*0.5f, size.height * 0.9f))
                                        drawCircle(Color.Blue, radius = 5f, center = Offset(size.width*0.75f, size.height * 0.9f))
                                    })
                                Text(text = "${it.stops.size} stops")
                            }
                            Spacer(modifier = Modifier.width(24.dp))
                        }
                    Box(modifier = Modifier.height(40.dp), contentAlignment = Alignment.Center) {
                        Text( textAlign = TextAlign.Center, text = it.routeStops.last().stops.last().nameTc, fontSize = 16.sp)
                    }

                }
                Divider(Modifier.padding(horizontal = 16.dp).fillMaxWidth())
            }
            }
        }
    }

@Composable
fun CollapsableRouteStopList(routeStopsList:List<RecommendedRoute.Route>){
    if (routeStopsList.isEmpty())
        return
    val iterator = routeStopsList.iterator().withIndex()
    Column() {
        iterator.next().let {
            CollapsableRouteStop(!iterator.hasNext(), route =it.value )
        }
        while (iterator.hasNext()){
            DotView()
            iterator.next().let {
                CollapsableRouteStop(isLast = !iterator.hasNext(), route = it.value )
            }
        }
    }

}

@Composable
fun CollapsableRouteStop(isLast: Boolean,route:RecommendedRoute.Route){
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(route.stops.first().nameTc, fontSize = 24.sp)
        Text(text = "${route.route.route} To: ${route.route.destTc}", fontSize = 16.sp)
        if (isLast){
            DotView()
            Text(route.stops.last().nameTc, fontSize = 24.sp)
        }
    }
}

@Composable
fun DotView(){
    Canvas(modifier = Modifier
        .size(25.dp)
        .padding(horizontal = 32.dp), onDraw = {
        drawCircle(Color.Blue, radius = 5f, center = Offset(0f, size.height *0.25f))
        drawCircle(Color.Blue, radius = 5f, center = Offset(0f, size.height * 0.5f))
        drawCircle(Color.Blue, radius = 5f, center = Offset(0f, size.height * 0.75f))
    })
}