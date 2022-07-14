package cs.hku.hktransportandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cs.hku.hktransportandroid.repository.APIRepository
import cs.hku.hktransportandroid.repository.RecommendedRoute
import cs.hku.hktransportandroid.repository.Stop
import cs.hku.hktransportandroid.repository.StopTextSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(): ViewModel() {
    @Inject
    lateinit var apiRepository: APIRepository
    private val _sourceStopText = MutableStateFlow("")
    val sourceStopText: Flow<String> = _sourceStopText
    private val _sourceStop = MutableStateFlow<StopTextSearch?>(null)
    val sourStop :Flow<StopTextSearch?> = _sourceStop
    private val _destinationStopText = MutableStateFlow("")
    val destinationStopText = _destinationStopText
    private val _destinationStop = MutableStateFlow<StopTextSearch?>(null)
    val destinationStop:Flow<StopTextSearch?> = _destinationStop
    var searchJob: Job? = null
    private val _searchStopsResult  = MutableStateFlow<StopQueryResultList?>(null)
    val searchStopsResult :Flow<StopQueryResultList?> = _searchStopsResult
    private val _recommendedRouteDisplay = MutableStateFlow<RecommendedRouteDisplay?> (null)
    val recommendedRouteDisplay = _recommendedRouteDisplay
    var queryJob :Job? = null


    fun onSourceStopTextChanged(text:String){
        _sourceStopText.value = text
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val stops = apiRepository.searchStop(text)
            _searchStopsResult.value = StopQueryResultList(StopQueryType.SOURCE,stops)
        }
    }
    fun onSourceStopClear(){
        searchJob?.cancel()
        queryJob?.cancel()
        _sourceStopText.value = ""
        _sourceStop.value = null
        _recommendedRouteDisplay.value = null
    }

    fun onDestinationStopTextChanged(text: String){
        _destinationStopText.value = text
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val stops = apiRepository.searchStop(text)
            _searchStopsResult.value = StopQueryResultList(StopQueryType.DESTINATION,stops)
        }
        checkApi()
    }

    fun onDestinationStopClear(){
        searchJob?.cancel()
        queryJob?.cancel()
        _destinationStopText.value = ""
        _destinationStop.value = null
        _recommendedRouteDisplay.value = null
    }

    fun onStopSelect(type:StopQueryType,stopTextSearch: StopTextSearch){
        when(type){
            StopQueryType.SOURCE -> {
                _sourceStop.value = stopTextSearch
            }
            StopQueryType.DESTINATION ->{
                _destinationStop.value = stopTextSearch
            }
        }
        _searchStopsResult.value = null
        checkApi()
    }
    private fun checkApi(){
        val sourceStop = _sourceStop.value
        val destStop = _destinationStop.value
        if (queryJob?.isActive ==true||sourceStop==null||destStop==null)
            return
        viewModelScope.launch {
            val recommendedRoute = apiRepository.queryRoute(sourceStop.id,destStop.id)
            _recommendedRouteDisplay.value = recommendedRoute?.let { RecommendedRouteDisplay(RecommendRouteType.FOUND,it) }
        }
    }

}

data class StopQueryResultList(val type:StopQueryType, val stops:List<StopTextSearch>)
enum class StopQueryType{SOURCE,DESTINATION}

data class RecommendedRouteDisplay(
    val type:RecommendRouteType,
    val recommendedRoutes: List<RecommendedRoute>?
)
enum class RecommendRouteType{FOUND,NO_RESULT}