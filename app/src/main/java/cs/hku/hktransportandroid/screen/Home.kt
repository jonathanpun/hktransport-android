package cs.hku.hktransportandroid.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cs.hku.hktransportandroid.HomeViewModel
import cs.hku.hktransportandroid.screen.view.StopEta
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(navController: NavController, viewModel: HomeViewModel ){
    val rememberCoroutineScope = rememberCoroutineScope()
    val currentModelRouteState = remember { mutableStateOf("") }
    val currentModelStopState = remember { mutableStateOf("") }
    val map = viewModel.mapStopFlow.collectAsState(initial = null)
    val modelBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    ModalBottomSheetLayout(sheetState = modelBottomSheetState, sheetContent = {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable {
                    viewModel.removeSavedPoint(currentModelRouteState.value,currentModelStopState.value)
                     rememberCoroutineScope.launch {
                         modelBottomSheetState.hide()
                     }
                }) {
                Icon(
                    Icons.Filled.Add, modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    contentDescription = "")
                Spacer(modifier = Modifier.width(4.dp))
                Text(fontSize = 24.sp, text = "Remove")
            }
        }
    }){
        Scaffold(topBar = { TopAppBar(title = { Text(text = "Home")})}) { padding->
            Column(modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)) {
                map.value?.map { entry->
                    Text(text = entry.key.nameTc)
                    entry.value.map {
                        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                            StopEta(stopEta = it, onLongClick = {
                                currentModelRouteState.value= it.route
                                currentModelStopState.value = entry.key.stop
                                rememberCoroutineScope.launch {
                                    modelBottomSheetState.show()
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}