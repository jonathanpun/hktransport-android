package cs.hku.hktransportandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs.hku.hktransportandroid.repository.APIRepository
import cs.hku.hktransportandroid.repository.Route
import cs.hku.hktransportandroid.repository.Stop
import cs.hku.hktransportandroid.repository.StopTextSearch
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

class SearchViewModel : ViewModel() {
    val repository = APIRepository()

    val _searchKeyword = MutableStateFlow<String?>(null)
    val searchKeyword = _searchKeyword as Flow<String?>
    val _searchHistory = MutableStateFlow<SearchResult?>(
        SearchResult(
            route = listOf(
                Route(
                    bound = "I",
                    destEn = "MEI FOO",
                    destSc = "美孚",
                    destTc = "美孚",
                    origEn = "HIN KENG",
                    origSc = "显径",
                    origTc = "顯徑",
                    route = "46X",
                    serviceType = "1",
                    lineGeometry = emptyList()
                )
            ),
            stops = emptyList()
//            stops = listOf(
//                Stop(lat = "22.338347",
//                    long = "114.189704",
//                    nameEn = "MORSE PARK SPORTS CENTRE",
//                    nameSc = "摩士公园体育馆",
//                    nameTc = "摩士公園體育館",
//                    stop = "713BF17AF24FE4DA",
//                eta = null)
//            )
        )
    )
    val searchHistory = _searchHistory as Flow<SearchResult?>

    val list = _searchKeyword.debounce(1000).transformLatest { q ->
        if (q.isNullOrBlank())
            emit(null)
        else {
            val stops = viewModelScope.async { repository.searchStop(q) }
            val routes = viewModelScope.async { repository.searchRoute(q, 10) }
            emit(
                SearchResult(
                    stops = stops.await(),
                    route = routes.await()
                )
            )
        }
    }

    fun onNewSearchKeyword(q: String) {
        _searchKeyword.value = q
    }
}

data class SearchResult(
    val route: List<Route>,
    val stops: List<StopTextSearch>
)