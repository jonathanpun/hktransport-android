package cs.hku.hktransportandroid.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cs.hku.hktransportandroid.Entity.SavedPoint
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedPointDao {
    @Query("SELECT * FROM SavedPoint")
    fun getUserSavedPoints(): Flow<List<SavedPoint>>
    @Insert
    fun addSavedPoints(savedPoint: SavedPoint)
    @Delete
    fun removeSavedPoint(savedPoint: SavedPoint)
}