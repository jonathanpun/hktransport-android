package cs.hku.hktransportandroid.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cs.hku.hktransportandroid.SearchViewModel

@Composable
fun Search(navController: NavController, viewModel: SearchViewModel = viewModel()){
    val searchKeyword = viewModel.searchKeyword.collectAsState(initial = "")
    val resultList = viewModel.list.collectAsState(initial = emptyList())
    Column() {
        TextField(value = searchKeyword.value.orEmpty(), onValueChange = {
            viewModel.onNewSearchKeyword(it)
        })
        resultList.value.forEach {
            Text(it.nameSc, modifier = Modifier.clickable {
                navController.navigate("stop/${it.stop}")
            })
        }
    }
}