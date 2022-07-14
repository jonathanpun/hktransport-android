package cs.hku.hktransportandroid

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs.hku.hktransportandroid.Entity.SavedPoint
import cs.hku.hktransportandroid.Entity.StopEtaGrouped
import cs.hku.hktransportandroid.Entity.group
import cs.hku.hktransportandroid.repository.APIRepository
import cs.hku.hktransportandroid.repository.Stop
import cs.hku.hktransportandroid.repository.StopEta
import cs.hku.hktransportandroid.repository.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class StopViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle, val repository:APIRepository, val userPreferenceRepository: UserPreferenceRepository): ViewModel() {
    private val stopTextSearchId:Int = savedStateHandle["id"]!!
    private val _stops = MutableStateFlow<List<Stop>?>(null)
    val stops = _stops as Flow<List<Stop>?>
    val _stopEta = MutableStateFlow<List<StopEtaGrouped>?>(null)
    val stopEta = _stopEta as Flow<List<StopEtaGrouped>?>
    init {
        viewModelScope.launch {
            val stopTextSearch= repository.getStopTextSearch(stopTextSearchId)
            val stops = stopTextSearch.stopsIds.map { async {  repository.getStop(it)} }.awaitAll()
            _stops.value = stops
            val stopEta = stops.map { async { repository.getStopEta(it.stop).group(it.stop) } }.awaitAll().flatten()
            _stopEta.value =stopEta

        }
    }
    fun saveRouteStop(stop:String,route:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                userPreferenceRepository.saveStop(SavedPoint(SavedPoint.SavedPointType.ROUTE_STOP,route,stop))
            }
        }
    }
}