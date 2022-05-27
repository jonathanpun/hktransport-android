package cs.hku.hktransportandroid

import androidx.lifecycle.ViewModel
import cs.hku.hktransportandroid.repository.APIRepository
import cs.hku.hktransportandroid.repository.Stop
import kotlinx.coroutines.flow.*

class SearchViewModel:ViewModel() {
    val repository = APIRepository()

    val _searchKeyword = MutableStateFlow<String?>(null)
    val searchKeyword = _searchKeyword as Flow<String?>

    val list = _searchKeyword.debounce(1000).transformLatest {
        if (it.isNullOrBlank())
            emptyList<Stop>()
        else
            emit(repository.getStop(it))
    }

    fun onNewSearchKeyword(q:String){
        _searchKeyword.value = q
    }
}