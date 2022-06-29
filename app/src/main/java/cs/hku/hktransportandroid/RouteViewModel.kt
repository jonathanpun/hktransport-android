package cs.hku.hktransportandroid

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs.hku.hktransportandroid.repository.APIRepository
import cs.hku.hktransportandroid.repository.Route
import cs.hku.hktransportandroid.repository.Stop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RouteViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle,private val apiRepository: APIRepository):ViewModel() {
    private val routeId = savedStateHandle.get<String>("route").orEmpty()
    private val _route = MutableStateFlow<Route?>(null)
    val route = _route as Flow<Route?>
    private val _selectedIndex = MutableStateFlow(0)
    val selectedIndex = _selectedIndex as Flow<Int>
    private val _routeStop = MutableStateFlow<List<Stop>?>(null)
    val routeStop = _routeStop as Flow<List<Stop>?>
    init {
        viewModelScope.launch {
            val route= apiRepository.getRouteWithBoundAndServiceType(routeId,"I","1")
            _route.value = route
            val routeStop = apiRepository.getRouteStop(routeId,"I","1")
            val routeStopsEta =   viewModelScope.async {  apiRepository.getRouteEta(routeId,"1")}.await().filter { it.dir=="I" }.groupBy { it.seq }
            _routeStop.value= routeStop.mapIndexed { index, stop -> stop.copy(eta= routeStopsEta[index+1]) }
        }
    }

    fun setSelectedIndex(index:Int){
        _selectedIndex.value = index
    }

}