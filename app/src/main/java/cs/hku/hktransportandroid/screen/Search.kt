package cs.hku.hktransportandroid.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cs.hku.hktransportandroid.SearchResult
import cs.hku.hktransportandroid.SearchViewModel

@Composable
fun Search(navController: NavController, viewModel: SearchViewModel = viewModel()) {
    val searchKeyword = viewModel.searchKeyword.collectAsState(initial = "")
    val resultList = viewModel.list.collectAsState(initial = null)
    Scaffold(topBar = {TopAppBar(title = { Text("Search") })}) {
        Column(Modifier.padding(it)) {
            TextField(modifier = Modifier.fillMaxWidth(), value = searchKeyword.value.orEmpty(), onValueChange = {
                viewModel.onNewSearchKeyword(it)
            })
            resultList.value?.let { searchResult->
                if(searchResult.route.isNotEmpty()){
                    Text(text = "Route", modifier = Modifier.padding(start = 16.dp), fontSize = 16.sp)
                    searchResult.route.map { 
                        Text(text = it.route, modifier = Modifier.padding(start = 24.dp), fontSize = 14.sp)
                    }
                }
                if (searchResult.stops.isNotEmpty()){
                    Text(text = "Stop", modifier = Modifier.padding(start = 16.dp), fontSize = 16.sp)
                    searchResult.stops.map {
                        Text(text = it.nameTc, modifier = Modifier.padding(start = 24.dp), fontSize = 14.sp)
                    }
                }
                
            }
        }
    }
}