package cs.hku.hktransportandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs.hku.hktransportandroid.Entity.StopEtaGrouped
import cs.hku.hktransportandroid.Entity.group
import cs.hku.hktransportandroid.repository.APIRepository
import cs.hku.hktransportandroid.repository.Stop
import cs.hku.hktransportandroid.repository.StopEta
import cs.hku.hktransportandroid.repository.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository :APIRepository,
                                        private val userPreferenceRepository :UserPreferenceRepository
):ViewModel() {
    private val _stopEtaMap = MutableStateFlow<Map<Stop,List<StopEtaGrouped>>?>(null)
    val stopEtaMap = _stopEtaMap as Flow<Map<Stop, List<StopEtaGrouped>>?>
    init {
        viewModelScope.launch {
            val stopId = userPreferenceRepository.getSavedStop()
            val stopList = stopId.map {
                async {  repository.getStop(it)}
            }.awaitAll()
            _stopEtaMap.value = stopList.map {
                async {
                    it to repository.getStopEta(it.stop).group()
                }
            }.awaitAll().toMap()
        }

    }



}