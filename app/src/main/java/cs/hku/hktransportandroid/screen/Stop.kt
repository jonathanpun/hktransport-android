package cs.hku.hktransportandroid.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import cs.hku.hktransportandroid.StopViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import cs.hku.hktransportandroid.Entity.StopEtaGrouped
import cs.hku.hktransportandroid.screen.view.StopEta
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Stop(
    navController: NavController,
    viewModel: StopViewModel
) {
    val colors = arrayOf(Color.Black,Color.Blue,Color.Green,Color.Red,Color.Cyan)
    val rememberCoroutineScope = rememberCoroutineScope()
    val stops = viewModel.stops.collectAsState(initial = null)
    val stopEta = viewModel.stopEta.collectAsState(initial = null)
    val currentModelRouteState = remember { mutableStateOf<StopEtaGrouped?>(null) }
    val cameraPositionState = rememberCameraPositionState {
        //TST
        position = CameraPosition.fromLatLngZoom(LatLng(22.29735258383955, 114.17205794244728), 17f)
    }
    stops.value?.let {
        val bounds = LatLngBounds.builder().apply {
            it.map { include(LatLng(it.lat.toDouble(),it.long.toDouble())) }
        }.build()
        cameraPositionState.move(
            CameraUpdateFactory.newLatLngBounds(bounds,50)
        )
    }
    val modelBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scrollState = rememberScrollState()
    ModalBottomSheetLayout(sheetState = modelBottomSheetState, sheetContent = {
        Column(modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable {
                    currentModelRouteState.value?.let { viewModel.saveRouteStop(it.route, it.stop) }
                    rememberCoroutineScope.launch {
                        modelBottomSheetState.hide()
                    }
                }) {
                Icon(Icons.Filled.Add, modifier = Modifier
                    .width(24.dp)
                    .height(24.dp),
                contentDescription = "")
                Spacer(modifier = Modifier.width(4.dp))
                Text(fontSize = 24.sp, text = "Save")
            }
        }
    }) {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(stops.value?.firstOrNull()?.nameTc.orEmpty())
                })
            }
        ) {
            Column(Modifier.padding(it)) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    cameraPositionState = cameraPositionState
                ) {
                    stops.value?.let {
                        it.map {
                            Marker(
                                state = MarkerState(
                                    position = (LatLng(
                                        it.lat.toDouble(),
                                        it.long.toDouble()
                                    ))
                                ),
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    stopEta.value?.mapIndexed {i, it->
                        StopEta(stopEta = it, onLongClick = {
                            currentModelRouteState.value = it
                            rememberCoroutineScope.launch {
                                modelBottomSheetState.show()
                            }
                        },Color.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

