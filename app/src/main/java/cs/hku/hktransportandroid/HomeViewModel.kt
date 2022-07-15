package cs.hku.hktransportandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cs.hku.hktransportandroid.Entity.SavedPoint
import cs.hku.hktransportandroid.Entity.StopEtaGrouped
import cs.hku.hktransportandroid.Entity.group
import cs.hku.hktransportandroid.repository.APIRepository
import cs.hku.hktransportandroid.repository.Stop
import cs.hku.hktransportandroid.repository.StopEta
import cs.hku.hktransportandroid.repository.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository :APIRepository,
                                        private val userPreferenceRepository :UserPreferenceRepository,
):ViewModel() {
    private val _nearStops = MutableStateFlow<Map<Stop,List<StopEtaGrouped>>?>(null)
    val nearStops:Flow<Map<Stop,List<StopEtaGrouped>>?> = _nearStops
   val savedPoint = userPreferenceRepository.getSavedStop().transform {
       val stopEtaMap = it.map { savedPoint->
           //todo other saved point type
           if (savedPoint.type != SavedPoint.SavedPointType.ROUTE_STOP)
               throw  Exception()
           viewModelScope.async {
               val stop = repository.getStop(savedPoint.stop)
               val stopEta = repository.getStopEta(savedPoint.stop)
               stop to stopEta.filter {eta-> eta.route== savedPoint.route }.group(stop.stop)
           }
       }.awaitAll().toMap()
        emit(stopEtaMap)
   }

    fun removeSavedPoint(route:String,stop:String){
        viewModelScope.launch(Dispatchers.IO) {
            userPreferenceRepository.removeSavedStop(SavedPoint(SavedPoint.SavedPointType.ROUTE_STOP,route,stop))
        }
    }

    fun onLocationUpdated(lat:Double,lng:Double){
        viewModelScope.launch {
            val nearStops = repository.getNearStop(lat, lng)
            val stopsEtas = nearStops.map { async { it to repository.getStopEta(it.stop).group(it.stop) } }.awaitAll().toMap()
            _nearStops.value = stopsEtas
        }
    }

}