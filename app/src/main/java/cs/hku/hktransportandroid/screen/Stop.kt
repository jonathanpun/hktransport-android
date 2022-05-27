package cs.hku.hktransportandroid.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import cs.hku.hktransportandroid.StopViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Stop(navController: NavController,stopId:String,viewModel: StopViewModel = viewModel(factory = object :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  StopViewModel(stopId) as T
    }
})){


}