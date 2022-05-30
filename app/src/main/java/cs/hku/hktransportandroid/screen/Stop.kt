package cs.hku.hktransportandroid.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import cs.hku.hktransportandroid.StopViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import cs.hku.hktransportandroid.repository.StopEta

@Composable
fun Stop(navController: NavController,stopId:String,viewModel: StopViewModel = viewModel(factory = object :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  StopViewModel(stopId) as T
    }
})){
    val stop = viewModel.stop.collectAsState(initial = null)
    val stopEta = viewModel.stopEta.collectAsState(initial = null)
    val cameraPositionState = rememberCameraPositionState {
        //TST
        position = CameraPosition.fromLatLngZoom(LatLng(22.29735258383955, 114.17205794244728), 10f)
    }
    stop.value?.let {
        cameraPositionState.move(CameraUpdateFactory.newLatLng(LatLng(it.lat.toDouble(),it.long.toDouble())))
    }
    Column() {
        Text(stop.value?.nameTc.orEmpty())
        GoogleMap(
            modifier = Modifier
                .width(400.dp)
                .height(400.dp),
            cameraPositionState = cameraPositionState
        )
        stopEta.value?.map {
            StopEta(stopEta = it)
        }
    }
}

@Composable
fun StopEta(stopEta: StopEta){
    Column() {
        Text(text = stopEta.route)
        Row() {
            Text(text = stopEta.destTc)
            Text(text = stopEta.eta.orEmpty())
        }
    }
}