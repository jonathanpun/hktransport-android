package cs.hku.hktransportandroid.repository

import androidx.room.Dao
import cs.hku.hktransportandroid.Entity.SavedPoint
import cs.hku.hktransportandroid.dao.Database
import cs.hku.hktransportandroid.dao.SavedPointDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferenceRepository @Inject constructor(private val savedPointDao: SavedPointDao){
    fun saveStop(savedPoint: SavedPoint){
        savedPointDao.addSavedPoints(savedPoint)
    }
    fun getSavedStop(): Flow<List<SavedPoint>> {
        return savedPointDao.getUserSavedPoints()
    }
    fun removeSavedStop(savedPoint: SavedPoint){
        savedPointDao.removeSavedPoint(savedPoint)
    }
}