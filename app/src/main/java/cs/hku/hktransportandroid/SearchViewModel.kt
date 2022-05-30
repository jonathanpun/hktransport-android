package cs.hku.hktransportandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs.hku.hktransportandroid.repository.APIRepository
import cs.hku.hktransportandroid.repository.Route
import cs.hku.hktransportandroid.repository.Stop
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

class SearchViewModel:ViewModel() {
    val repository = APIRepository()

    val _searchKeyword = MutableStateFlow<String?>(null)
    val searchKeyword = _searchKeyword as Flow<String?>

    val list = _searchKeyword.debounce(1000).transformLatest { q->
        if (q.isNullOrBlank())
            emit(null)
        else{
            val stops = viewModelScope.async { repository.searchStop(q) }
            val routes = viewModelScope.async { repository.searchRoute(q,10) }
            emit(SearchResult(
                stops = stops.await(),
                route = routes.await()
            ))
        }
    }

    fun onNewSearchKeyword(q:String){
        _searchKeyword.value = q
    }
}

data class SearchResult(
    val route:List<Route>,
    val stops:List<Stop>
)