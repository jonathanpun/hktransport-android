package cs.hku.hktransportandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs.hku.hktransportandroid.repository.APIRepository
import cs.hku.hktransportandroid.repository.StopEta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    val repository = APIRepository()
    val list = MutableStateFlow<List<StopEta>?>(null)

    init {
        viewModelScope.launch {
            list.value=repository.getStopEta("A60AE774B09A5E44")
        }
    }



}