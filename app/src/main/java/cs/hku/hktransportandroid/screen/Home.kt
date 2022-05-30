package cs.hku.hktransportandroid.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cs.hku.hktransportandroid.HomeViewModel
import cs.hku.hktransportandroid.screen.view.StopEta

@Composable
fun Home(navController: NavController, viewModel: HomeViewModel = viewModel()){
    val map = viewModel.stopEtaMap.collectAsState(initial = null)
   Scaffold(topBar = { TopAppBar(title = { Text(text = "Home")})}) { padding->
       Column(modifier = Modifier
           .padding(padding)
           .padding(horizontal = 16.dp)) {
           map.value?.map {
               Text(text = it.key.nameTc)
               it.value.map {
                   Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                       StopEta(stopEta = it)
                   }
               }
           }
       }
   }
       
}