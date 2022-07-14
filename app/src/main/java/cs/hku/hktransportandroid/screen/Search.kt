package cs.hku.hktransportandroid.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.Placeholder
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cs.hku.hktransportandroid.SearchResult
import cs.hku.hktransportandroid.SearchViewModel

@Composable
fun Search(navController: NavController, viewModel: SearchViewModel) {
    val searchKeyword = viewModel.searchKeyword.collectAsState(initial = "")
    val searchHistory = viewModel.searchHistory.collectAsState(initial = null)
    val resultList = viewModel.list.collectAsState(initial = null)
    Scaffold(topBar = { TopAppBar(title = { Text("Search") }) }) {
        Column(Modifier.padding(it)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 4.dp),
                    placeholder = {Text("Route/Bus stop")},
                    leadingIcon = {Icon (Icons.Filled.Search,
                        contentDescription = "")}
                , value = searchKeyword.value.orEmpty(), onValueChange = {
                viewModel.onNewSearchKeyword(it)
            }, )
            }
            val searchResult = resultList.value?:searchHistory.value
            if(resultList.value == null&&searchHistory.value!=null){
                Text(text = "Search history",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(6.dp))
            }
            if (searchResult!=null){
                if (searchResult.route.isNotEmpty()) {
                    Text(
                        text = "Route",
                        modifier = Modifier.padding(start = 16.dp),
                        fontSize = 20.sp
                    )
                    searchResult.route.map {
                        Column(modifier = Modifier
                            .padding(start = 24.dp)
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("routes/${it.route}/${it.bound}/${it.serviceType}")
                            },) {
                            Text(
                                text = it.route,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "To: ${it.destTc}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))

                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                if (searchResult.stops.isNotEmpty()) {
                    Text(
                        text = "Stop",
                        modifier = Modifier.padding(start = 16.dp),
                        fontSize = 20.sp
                    )
                    searchResult.stops.map {
                        Text(text = it.nameTc, modifier = Modifier
                            .padding(start = 24.dp)
                            .fillMaxWidth()
                            .clickable {
                                //navController.navigate("stop/${it.stop}")
                            }, fontSize = 16.sp
                        )
                    }
                }

            }
        }
    }
}