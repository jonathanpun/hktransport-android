package cs.hku.hktransportandroid.repository

import androidx.room.Dao
import cs.hku.hktransportandroid.Entity.SavedPoint

class UserPreferenceRepository {
    fun saveStop(savedPoint: SavedPoint){
        //todo
    }
    fun getSavedStop():List<String>{
        return listOf("A66159E033DDEA04")
    }
}