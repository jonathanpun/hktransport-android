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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StopViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle, val repository:APIRepository, val userPreferenceRepository: UserPreferenceRepository): ViewModel() {
    val stopId:String = savedStateHandle["id"]!!
    val _stop = MutableStateFlow<Stop?>(null)
    val stop = _stop as Flow<Stop?>
    val _stopEta = MutableStateFlow<List<StopEtaGrouped>?>(null)
    val stopEta = _stopEta as Flow<List<StopEtaGrouped>?>
    init {
        viewModelScope.launch {
            val stop= repository.getStop(stopId)
            _stop.value = stop
            val stopEta = repository.getStopEta(stopId)
            _stopEta.value =stopEta.group()

        }
    }
    fun saveRouteStop(route:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _stop.value?.let { userPreferenceRepository.saveStop(SavedPoint(0,SavedPoint.SavedPointType.ROUTE_STOP,route,it.stop)) }
            }
        }
    }
}