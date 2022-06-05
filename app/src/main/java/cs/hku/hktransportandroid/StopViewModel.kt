package cs.hku.hktransportandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs.hku.hktransportandroid.Entity.StopEtaGrouped
import cs.hku.hktransportandroid.Entity.group
import cs.hku.hktransportandroid.repository.APIRepository
import cs.hku.hktransportandroid.repository.Stop
import cs.hku.hktransportandroid.repository.StopEta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StopViewModel(val stopId:String): ViewModel() {
    private val repository = APIRepository()
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
}