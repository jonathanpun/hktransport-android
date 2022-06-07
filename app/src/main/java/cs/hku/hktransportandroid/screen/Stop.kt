package cs.hku.hktransportandroid.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import cs.hku.hktransportandroid.StopViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import cs.hku.hktransportandroid.screen.view.StopEta

@Composable
fun Stop(
    navController: NavController,
    viewModel: StopViewModel
) {
    val stop = viewModel.stop.collectAsState(initial = null)
    val stopEta = viewModel.stopEta.collectAsState(initial = null)
    val cameraPositionState = rememberCameraPositionState {
        //TST
        position = CameraPosition.fromLatLngZoom(LatLng(22.29735258383955, 114.17205794244728), 17f)
    }
    stop.value?.let {
        cameraPositionState.move(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    it.lat.toDouble(),
                    it.long.toDouble()
                )
            )
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(stop.value?.nameTc.orEmpty())
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
                stop.value?.let {
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
            Spacer(modifier = Modifier.height(12.dp))
            stopEta.value?.map {
                StopEta(stopEta = it, addAction = {
                    viewModel.saveRouteStop(it.route)
                })
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

    }
}

