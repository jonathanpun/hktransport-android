package cs.hku.hktransportandroid.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cs.hku.hktransportandroid.HomeViewModel

@Composable
fun Home(navController: NavController, viewModel: HomeViewModel = viewModel()){
    val eta =viewModel.list.collectAsState()
    eta.value?.map { Text(it.destEn.orEmpty())  }
}